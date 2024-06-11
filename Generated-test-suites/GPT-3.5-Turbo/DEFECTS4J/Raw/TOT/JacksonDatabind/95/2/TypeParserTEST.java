package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TypeParserTest {

    @Mock
    private TypeFactory typeFactory;

    private TypeParser typeParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse_WithEmptyString_ShouldThrowIllegalArgumentException() {
        // Arrange
        String canonical = "";
        
        // Act and Assert
        try {
            typeParser.parse(canonical);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Assertion
            assertEquals("Failed to parse type '', remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_WithNonGenericClass_ShouldReturnJavaType() {
        // Arrange
        String canonical = "java.lang.String";
        JavaType expectedType = new JavaType();
        when(typeFactory._fromClass(null, String.class, null)).thenReturn(expectedType);

        // Act
        JavaType resultType = typeParser.parse(canonical);

        // Assert
        verify(typeFactory)._fromClass(null, String.class, null);
        assertSame(expectedType, resultType);
    }

    @Test
    public void testParse_WithGenericClass_ShouldReturnJavaType() {
        // Arrange
        String canonical = "java.util.List<java.lang.String>";
        JavaType expectedType = new JavaType();
        when(typeFactory._fromClass(null, List.class, createTypeBindings(String.class))).thenReturn(expectedType);

        // Act
        JavaType resultType = typeParser.parse(canonical);

        // Assert
        verify(typeFactory)._fromClass(null, List.class, createTypeBindings(String.class));
        assertSame(expectedType, resultType);
    }

    @Test
    public void testParse_WithUnexpectedTokensAfterCompleteType_ShouldThrowIllegalArgumentException() {
        // Arrange
        String canonical = "java.lang.String unexpected";
        
        // Act and Assert
        try {
            typeParser.parse(canonical);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Assertion
            assertEquals("Failed to parse type 'java.lang.String unexpected' (remaining: ' unexpected'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParseType_WithEmptyString_ShouldThrowIllegalArgumentException() {
        // Arrange
        String token = "";
        MyTokenizer tokens = new MyTokenizer(token);
        
        // Act and Assert
        try {
            typeParser.parseType(tokens);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Assertion
            assertEquals("Failed to parse type '': Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseType_WithNonGenericClass_ShouldReturnJavaType() {
        // Arrange
        String token = "java.lang.String";
        MyTokenizer tokens = new MyTokenizer(token);
        Class<?> expectedClass = String.class;
        when(typeFactory.findClass("java.lang.String")).thenReturn(expectedClass);
        JavaType expectedType = new JavaType();
        when(typeFactory._fromClass(null, expectedClass, null)).thenReturn(expectedType);

        // Act
        JavaType resultType = typeParser.parseType(tokens);

        // Assert
        verify(typeFactory).findClass("java.lang.String");
        verify(typeFactory)._fromClass(null, expectedClass, null);
        assertSame(expectedType, resultType);
    }

    @Test
    public void testParseType_WithGenericClass_ShouldReturnJavaType() {
        // Arrange
        String token = "java.util.List";
        String commaToken = ",";
        String closingToken = ">";
        MyTokenizer tokens = new MyTokenizer(token);
        tokens.pushBack("<");
        tokens.pushBack(closingToken);
        tokens.pushBack(commaToken);
        tokens.pushBack(token);
        Class<?> expectedClass = List.class;
        when(typeFactory.findClass(token)).thenReturn(expectedClass);
        ArrayList<JavaType> parameterTypes = new ArrayList<JavaType>();
        JavaType parameterType = new JavaType();
        parameterTypes.add(parameterType);
        TypeBindings typeBindings = createTypeBindings(parameterType);
        when(typeFactory._fromClass(null, expectedClass, typeBindings)).thenReturn(new JavaType());

        // Act
        JavaType resultType = typeParser.parseType(tokens);

        // Assert
        verify(typeFactory).findClass(token);
        verify(typeFactory)._fromClass(null, expectedClass, typeBindings);
        assertSame(parameterType, resultType);
    }

    @Test
    public void testParseTypes_WithNonGenericClass_ShouldReturnEmptyList() {
        // Arrange
        String token = "java.lang.String";
        MyTokenizer tokens = new MyTokenizer(token);
        ArrayList<JavaType> expectedTypes = new ArrayList<>();

        // Act
        ArrayList<JavaType> resultTypes = typeParser.parseTypes(tokens);

        // Assert
        assertEquals(expectedTypes, resultTypes);
    }

    @Test
    public void testParseTypes_WithMultipleGenericClasses_ShouldReturnJavaTypeList() {
        // Arrange
        String token1 = "java.lang.String";
        String token2 = "java.util.List";
        String token3 = "java.util.Map";
        MyTokenizer tokens = new MyTokenizer(token3);
        tokens.pushBack(",");
        tokens.pushBack(token2);
        tokens.pushBack(",");
        tokens.pushBack(token1);
        ArrayList<JavaType> expectedTypes = new ArrayList<>();
        JavaType expectedType1 = new JavaType();
        expectedTypes.add(expectedType1);
        JavaType expectedType2 = new JavaType();
        expectedTypes.add(expectedType2);
        when(typeFactory._fromClass(null, String.class, null)).thenReturn(expectedType1);
        when(typeFactory._fromClass(null, List.class, createTypeBindings(String.class))).thenReturn(expectedType2);

        // Act
        ArrayList<JavaType> resultTypes = typeParser.parseTypes(tokens);

        // Assert
        verify(typeFactory)._fromClass(null, String.class, null);
        verify(typeFactory)._fromClass(null, List.class, createTypeBindings(String.class));
        assertEquals(expectedTypes, resultTypes);
    }

    @Test
    public void testParseTypes_WithUnexpectedEndOfString_ShouldThrowIllegalArgumentException() {
        // Arrange
        String token = ",";
        MyTokenizer tokens = new MyTokenizer(token);
        tokens.pushBack(token);
        
        // Act and Assert
        try {
            typeParser.parseTypes(tokens);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Assertion
            assertEquals("Failed to parse type '', remaining: ',': Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testFindClass_WithValidClassName_ShouldReturnClass() {
        // Arrange
        String className = "java.lang.String";
        Class<?> expectedClass = String.class;
        when(typeFactory.findClass(className)).thenReturn(expectedClass);

        // Act
        Class<?> resultClass = typeParser.findClass(className, new MyTokenizer(className));

        // Assert
        verify(typeFactory).findClass(className);
        assertSame(expectedClass, resultClass);
    }

    @Test
    public void testFindClass_WithInvalidClassName_ShouldThrowIllegalArgumentException() {
        // Arrange
        String className = "invalid.class.name";
        MyTokenizer tokens = new MyTokenizer(className);
        RuntimeException expectedException = new RuntimeException("Class not found");
        when(typeFactory.findClass(className)).thenThrow(expectedException);

        // Act and Assert
        try {
            typeParser.findClass(className, tokens);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Assertion
            assertEquals("Can not locate class 'invalid.class.name', problem: Class not found", e.getMessage());
        }
    }

    private TypeBindings createTypeBindings(Class<?> parameter) {
        return TypeBindings.create(parameter, null);
    }
}