import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactoryBuilder;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClassNameIdResolverTest {

    @InjectMocks
    private ClassNameIdResolver classUnderTest;

    private ObjectMapper objectMapper;
    private TypeFactory typeFactory;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        objectMapper = mock(ObjectMapper.class);
        typeFactory = new TypeFactoryBuilder().build();
        when(objectMapper.getTypeFactory()).thenReturn(typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classUnderTest.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // Not testing as it's not used with class name - based resolvers
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIdFromValueWithNullValue() {
        classUnderTest.idFromValue(null);
    }

    @Test
    public void testIdFromValueWithPrimitiveValue() {
        String result = classUnderTest.idFromValue(123);
        assertEquals("java.lang.Integer", result);
    }

    @Test
    public void testIdFromValueWithObjectValue() {
        String result = classUnderTest.idFromValue(new Object());
        assertNull(result);
    }

    @Test
    public void testIdFromValueAndTypeWithNullValue() {
        // Not testing as it delegates to idFromValue method
    }

    @Test
    public void testIdFromValueAndTypeWithPrimitiveValue() {
        String result = classUnderTest.idFromValueAndType(123, Integer.TYPE);
        assertEquals("java.lang.Integer", result);
    }

    @Test
    public void testIdFromValueAndTypeWithCustomClassValue() {
        Class<?> customClass = mock(Class.class);
        String result = classUnderTest.idFromValueAndType(new Object(), customClass);
        assertEquals(customClass.getName(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdFromValueAndTypeWithInvalidId() {
        classUnderTest.idFromValueAndType(new Object(), "invalidId");
    }

    @Test
    public void testTypeFromIdWithValidId() throws IOException {
        String validId = "java.lang.String";
        JavaType expectedType = typeFactory.constructType(String.class);
        JavaType resultType = classUnderTest.typeFromId(mock(DatabindContext.class), validId);
        assertEquals(expectedType, resultType);
    }

    @Test(expected = IOException.class)
    public void testTypeFromIdWithUnknownId() throws IOException {
        String unknownId = "com.non.existing.package.NonExistentClass";
        classUnderTest.typeFromId(mock(DatabindContext.class), unknownId);
    }

    @Test
    public void testTypeFromIdWithGenericId() throws IOException {
        String genericId = "java.util.ArrayList<java.lang.String>";
        JavaType expectedType = typeFactory.constructType(ArrayList.class, String.class);
        JavaType resultType = classUnderTest.typeFromId(mock(DatabindContext.class), genericId);
        assertEquals(expectedType, resultType);
    }

    @Test
    public void testIdFromWithEnumValue() {
        Enum testEnum = Enum.valueOf(MyEnum.class, "ONE");
        String result = classUnderTest._idFrom(testEnum, MyEnum.class, typeFactory);
        assertEquals("com.test.MyEnum", result);
    }

    @Test
    public void testIdFromWithInnerClassValue() {
        Class<?> outerClass = mock(Class.class);
        Class<?> innerClass = mock(Class.class);
        when(outerClass.getOuterClass()).thenReturn(null);
        when(outerClass.getName()).thenReturn("outerClass");
        when(innerClass.getName()).thenReturn("innerClass$1");
        when(innerClass.getOuterClass()).thenReturn(outerClass);

        String result = classUnderTest._idFrom(new Object(), innerClass, typeFactory);
        assertEquals("outerClass$innerClass$1", result);
    }

    @Test
    public void testIdFromWithArrayValue() {
        int[] array = {1, 2, 3};
        String result = classUnderTest._idFrom(array, int[].class, typeFactory);
        assertEquals("java.lang.int[]", result);
    }

    @Test
    public void testIdFromWithArrayListValue() {
        List<String> list = Arrays.asList(new String[]{"one", "two", "three"});
        String result = classUnderTest._idFrom(list, ArrayList.class, typeFactory);
        assertEquals("java.util.ArrayList", result);
    }

    @Test
    public void testIdFromWithEnumSetValue() {
        Set<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
        Class<MyEnum> enumClass = MyEnum.class;
        String result = classUnderTest._idFrom(enumSet, EnumSet.class, typeFactory);
        Class<Map.Entry<MyEnum, MyEnum>> entryClass = Map.Entry.class.getGenericType();
        String expected = typeFactory.constructCollectionType(EnumSet.class, enumClass).toCanonical();
        assertEquals(expected, result);
    }

    @Test
    public void testIdFromWithEnumMapValue() {
        Map<MyEnum, String> enumMap = new EnumMap<>(MyEnum.class);
        enumMap.putAll(Collections.singletonMap(MyEnum.ONE, "one"));
        Class<MyEnum> enumClass = MyEnum.class;
        String result = classUnderTest._idFrom(enumMap, EnumMap.class, typeFactory);
        Class<?> valueClass = String.class;
        String expected = typeFactory.constructMapType(EnumMap.class, enumClass, valueClass).toCanonical();
        assertEquals(expected, result);
    }
}