package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeParser.MyTokenizer;

/**
 * JUnit test cases for TypeParser class
 *
 * @author John Doe
 */
public class TypeParserTest {

    private TypeFactory typeFactory;
    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeFactory = mock(TypeFactory.class);
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse_ValidType_ReturnsCorrectJavaType() {
        String canonicalType = "java.util.List<java.lang.String>";
        JavaType expectedType = TypeBindings.constructParametrizedType(List.class, String.class);
        when(typeFactory.findClass("java.util.List")).thenReturn(List.class);
        when(typeFactory.findClass("java.lang.String")).thenReturn(String.class);

        JavaType actualType = typeParser.parse(canonicalType);

        assertEquals(expectedType, actualType);
        verify(typeFactory, times(2)).findClass(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidType_ThrowsIllegalArgumentException() {
        String canonicalType = "MyInvalidType";

        typeParser.parse(canonicalType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_TypeWithUnexpectedToken_ThrowsIllegalArgumentException() {
        String canonicalType = "java.util.List<java.lang.String<foo>";

        typeParser.parse(canonicalType);
    }

    @Test
    public void testParseType_ValidClass_ReturnsJavaType() {
        String className = "java.util.List";

        Class<?> expectedClass = List.class;

        when(typeFactory.findClass(className)).thenReturn(expectedClass);

        JavaType actualType = typeParser.parseType(new MyTokenizer(className));

        assertEquals(expectedClass, actualType.getRawClass());
        verify(typeFactory, times(1)).findClass(className);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_InvalidClass_ThrowsIllegalArgumentException() {
        String className = "MyInvalidClass";

        typeParser.parseType(new MyTokenizer(className));
    }

    @Test
    public void testParseTypes_OneType_ReturnsListWithSingleJavaType() {
        String canonicalType = "java.util.List<java.lang.String>";

        JavaType javaType = TypeBindings.constructParametrizedType(List.class, String.class);

        Class<?> listClass = List.class;
        Class<?> stringClass = String.class;
        when(typeFactory.findClass(listClass.getName())).thenReturn(listClass);
        when(typeFactory.findClass(stringClass.getName())).thenReturn(stringClass);

        List<JavaType> actualTypes = typeParser.parseTypes(new MyTokenizer(canonicalType));

        assertEquals(javaType, actualTypes.get(0));
        verify(typeFactory, times(2)).findClass(anyString());
    }

    @Test
    public void testParseTypes_MultipleTypes_ReturnsListWithJavaTypes() {
        String canonicalType = "java.util.List<java.lang.String>, java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";

        JavaType listOfStringType = TypeBindings.constructParametrizedType(List.class, String.class);
        JavaType mapOfStringToListOfIntegerType = TypeBindings.constructParametrizedType(Map.class, String.class, List.class, Integer.class);

        Class<?> listClass = List.class;
        Class<?> stringClass = String.class;
        Class<?> mapClass = Map.class;
        Class<?> integerClass = Integer.class;

        when(typeFactory.findClass(listClass.getName())).thenReturn(listClass);
        when(typeFactory.findClass(stringClass.getName())).thenReturn(stringClass);
        when(typeFactory.findClass(mapClass.getName())).thenReturn(mapClass);
        when(typeFactory.findClass(integerClass.getName())).thenReturn(integerClass);

        List<JavaType> actualTypes = typeParser.parseTypes(new MyTokenizer(canonicalType));

        assertEquals(Arrays.asList(listOfStringType, mapOfStringToListOfIntegerType), actualTypes);
        verify(typeFactory, times(4)).findClass(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_UnexpectedToken_ThrowsIllegalArgumentException() {
        String canonicalType = "java.util.List<java.lang.String>, <foo>";

        typeParser.parseTypes(new MyTokenizer(canonicalType));
    }

    @Test
    public void testFindClass_ValidClassName_ReturnsClassObject() {
        String className = "java.util.List";

        Class<?> expectedClass = List.class;

        when(typeFactory.findClass(className)).thenReturn(expectedClass);

        Class<?> actualClass = typeParser.findClass(className, new MyTokenizer(className));

        assertEquals(expectedClass, actualClass);
        verify(typeFactory, times(1)).findClass(className);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_InvalidClassName_ThrowsIllegalArgumentException() {
        String className = "MyInvalidClass";

        typeParser.findClass(className, new MyTokenizer(className));
    }
}