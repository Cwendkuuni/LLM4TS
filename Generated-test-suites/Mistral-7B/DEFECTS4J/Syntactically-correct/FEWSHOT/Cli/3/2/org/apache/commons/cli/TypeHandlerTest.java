package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateStringValue() {
        String str = "test";
        assertEquals(str, TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateObjectValue() {
        String className = "java.lang.String";
        String str = "test";
        Class clazz = ClassUtils.forName(className);

        Object obj = TypeHandler.createValue(str, clazz);
        assertNotNull(obj);
        assertEquals(str, obj.toString());
    }

    @Test
    public void testCreateNumberValue() {
        String numberString = "123";
        Number number = TypeHandler.createValue(numberString, Number.class);
        assertTrue(number instanceof Long);
        assertEquals(Long.parseLong(numberString), ((Long) number).longValue());

        String numberWithDecimalString = "123.45";
        number = TypeHandler.createValue(numberWithDecimalString, Number.class);
        assertTrue(number instanceof Double);
        assertEquals(Double.parseDouble(numberWithDecimalString), ((Double) number).doubleValue());
    }

    @Test
    public void testCreateDateValue() {
        String dateString = "2022-01-01";
        Date date = TypeHandler.createValue(dateString, Date.class);
        assertNotNull(date);
        assertEquals(DateUtils.parseDate(dateString, new String[]{"yyyy-MM-dd"}), date);
    }

    @Test
    public void testCreateClassValue() {
        String className = "java.lang.String";
        Class clazz = ClassUtils.forName(className);

        Object obj = TypeHandler.createValue("TestClass", clazz);
        assertNotNull(obj);
        assertEquals(clazz, obj.getClass());
    }

    @Test
    public void testCreateFileValue() {
        String filePath = "test.txt";
        File file = TypeHandler.createValue(filePath, File.class);
        assertNotNull(file);
        assertEquals(new File(filePath), file);
    }

    @Test
    public void testCreateURLValue() {
        String urlString = "http://test.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertNotNull(url);
        assertEquals(new URL(urlString), url);
    }

    @Test
    public void testCreateNumberValueWithInvalidString() {
        String numberString = "abcd";
        Number number = TypeHandler.createValue(numberString, Number.class);
        assertNull(number);
    }

    @Test
    public void testCreateDateValueWithInvalidString() {
        String dateString = "2022-01-01ABC";
        Date date = TypeHandler.createValue(dateString, Date.class);
        assertNull(date);
    }

    @Test
    public void testCreateClassValueWithInvalidString() {
        String className = "java.lan.String";
        Class clazz = ClassUtils.forName(className);

        Object obj = TypeHandler.createValue("TestClass", clazz);
        assertNull(obj);
    }

    @Test
    public void testCreateFileValueWithInvalidString() {
        String filePath = "test//.txt";
        File file = TypeHandler.createValue(filePath, File.class);
        assertNull(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateURLValueWithInvalidString() {
        String urlString = "test://test";
        TypeHandler.createValue(urlString, URL.class);
    }
}