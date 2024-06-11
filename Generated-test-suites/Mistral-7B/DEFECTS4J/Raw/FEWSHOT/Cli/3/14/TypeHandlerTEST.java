package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URL;

import java.util.Date;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringAndString() {
        String str = "foo";
        assertEquals(str, TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValue_StringAndClass() {
        String str = "foo";
        String className = "java.lang.String";
        assertEquals(str, TypeHandler.createValue(str, className));
        assertSame(String.class, Class.forName(className));
    }

    @Test
    public void testCreateValue_StringAndClassNotFoundException() {
        String str = "com.example.MyClass";
        String className = "com.example.MyClass";
        assertNull(TypeHandler.createValue(str, className));
    }

    @Test
    public void testCreateValue_StringAndInvalidClass() {
        String str = "123";
        String className = "java.util.Date";
        assertNull(TypeHandler.createValue(str, className));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderStringValue() {
        String str = "foo";
        assertEquals(str, TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderObjectValue() {
        String str = "com.example.MyClass";
        Class clazz = ClassUtils.forName(str, TypeHandler.class.getClassLoader());
        assertEquals(clazz, TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderNumberValue() {
        String str = "123";
        Number number = NumberUtils.createNumber(str);
        assertEquals(number, TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderDateValue() {
        String str = "2022-01-01";
        Date date = new Date(0L); // creating an instance with zero time to ensure equality checks work
        assertEquals(date, TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderClassValue() {
        String str = "java.util.Date";
        Class clazz = Class.forName(str);
        assertEquals(clazz, TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderFileValue() {
        String str = "test.txt";
        File file = new File(str);
        assertEquals(file, TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderExistingFileValue() {
        String str = "test.txt";
        File file = new File(str);
        assertEquals(file, TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderFilesValue() {
        String str = "test1.txt,test2.txt";
        String[] files = str.split(",");
        File[] filesArray = new File[files.length];
        for (int i = 0; i < files.length; i++) {
            filesArray[i] = new File(files[i]);
        }
        assertEquals(filesArray, TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilderURLValue() {
        String str = "http://example.com";
        URL url = new URL(str);
        assertEquals(url, TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_StringAndInvalidType() {
        String str = "123";
        assertNull(TypeHandler.createValue(str, "InvalidType.class"));
    }
}