package com.example.test;

import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.*;

public class ClassNameIdResolverTest {

    private static ClassNameIdResolver classResolver;

    @BeforeClass
    public static void setup() throws Exception {
        Object value = null;
        TypeFactory typeFactory = new TypeFactory();
        classResolver = new ClassNameIdResolver(null, typeFactory);
    }

    @Test
    public void testIdFromValue() throws Exception {
        // Arrange
        Object value = new Object();

        // Act
        String result = classResolver.idFromValue(value);

        // Assert
        assertEquals(value.getClass().getName(), result);
    }

    @Test
    public void testIdFromValueArrayValue() throws Exception {
        // Arrange
        Object value = new Object()[0];

        // Act
        String result = classResolver.idFromValue(value);

        // Assert
        String expected = "class [Ljava.lang.Object;";
        assertEquals(expected, result);
    }

    @Test
    public void testIdFromValueEnumValue() throws Exception {
        // Arrange
        Enum value = Enum.valueOf(MyEnum.class, "TEST");

        // Act
        String result = classResolver.idFromValue(value);

        // Assert
        String expected = MyEnum.class.getName();
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdFromValueNullValue() throws Exception {
        // Act
        String result = classResolver.idFromValue(null);

        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdFromValuePrimitiveTypeValue() throws Exception {
        // Arrange
        int value = 1;

        // Act and Assert
        classResolver.idFromValue(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdFromValueUnregisteredCustomClassValue() throws Exception {
        // Arrange
        Class unregisteredCustomClass = new Class("com.example.MyCustomClass") {
        };
        Object value = new unregisteredCustomClass();

        // Act and Assert
        classResolver.idFromValue(value);
    }

    @AfterClass
    public static void teardown() {
        classResolver = null;
    }
}

enum MyEnum {
    TEST
}