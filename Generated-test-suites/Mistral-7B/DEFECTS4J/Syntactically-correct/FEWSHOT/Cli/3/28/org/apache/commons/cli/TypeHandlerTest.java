package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import java.util.Date;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Class<String> stringClass = String.class;
        Object stringObj = "test";

        String str = "test";
        Object result = TypeHandler.createValue(str, stringClass);
        assertEquals(stringObj, result);
    }

    @Test
    public void testCreateValueStringClass() {
        Class<String> stringClass = String.class;
        String str = "test";

        Object result = TypeHandler.createValue(str, stringClass);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        assertNull(TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        Double number = 1.0;
        String strNumber = "1.0";

        Object result = TypeHandler.createValue(strNumber, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(number, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        Date date = new Date();
        String strDate = "2022-12-31";

        Object result = TypeHandler.createValue(strDate, PatternOptionBuilder.DATE_VALUE);
        assertEquals(date, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String className = "java.util.Date";
        Date date = new Date();

        Class<?> clazz = null;
        try {
            clazz = ClassUtils.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Constructor<?> constructor = null;
        try {
            constructor = ConstructorUtils.getConstructor(clazz);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object result = null;
        try {
            result = MethodUtils.invokeConstructor(constructor, new Object[]{});
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        assertNotNull(result);
        Object createdDate = TypeHandler.createValue(strDate, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(result, createdDate);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String filePath = "test.txt";
        File file = new File(filePath);

        Object result = TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE);
        assertEquals(file, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        String filePath = "test.txt";
        File file = new File(filePath);

        boolean exists = file.exists();

        Object result = TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertTrue(exists);
        assertEquals(file, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() {
        String urlString = "https://example.com";
        URL url = new URL(urlString);

        Object result = TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE);
        assertEquals(url, result);
    }
}