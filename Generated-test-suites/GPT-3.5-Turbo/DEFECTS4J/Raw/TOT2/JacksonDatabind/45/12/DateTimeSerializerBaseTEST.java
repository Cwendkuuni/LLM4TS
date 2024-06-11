package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.SerializerProvider;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ DateTimeSerializerBase.class })
public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializers;

    @Test
    public void testWithFormat() {
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTimeSerializerBase<DateTimeSerializerBaseTest> dateTimeSerializerBase = new DateTimeSerializerBase<>(DateTimeSerializerBaseTest.class, useTimestamp, customFormat);
        DateTimeSerializerBase<DateTimeSerializerBaseTest> result = dateTimeSerializerBase.withFormat(useTimestamp, customFormat);
        assertNotNull(result);
    }

    @Test
    public void testCreateContextual() throws Exception {
        // Mock property
        BeanProperty property = mock(BeanProperty.class);
        
        // Create object of DateTimeSerializerBase
        DateTimeSerializerBase<DateTimeSerializerBaseTest> dateTimeSerializerBase = new DateTimeSerializerBase<>(DateTimeSerializerBaseTest.class, useTimestamp, customFormat);
        
        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(serializers, property);
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<DateTimeSerializerBaseTest> dateTimeSerializerBase = new DateTimeSerializerBase<>(DateTimeSerializerBaseTest.class, useTimestamp, customFormat);
        boolean result = dateTimeSerializerBase.isEmpty(null);
        assertTrue(result);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<DateTimeSerializerBaseTest> dateTimeSerializerBase = new DateTimeSerializerBase<>(DateTimeSerializerBaseTest.class, useTimestamp, customFormat);
        JsonNode result = dateTimeSerializerBase.getSchema(serializers, typeHint);
        assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<DateTimeSerializerBaseTest> dateTimeSerializerBase = new DateTimeSerializerBase<>(DateTimeSerializerBaseTest.class, useTimestamp, customFormat);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
        // As this method doesn't return anything, no need to validate the result
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<DateTimeSerializerBaseTest> dateTimeSerializerBase = new DateTimeSerializerBase<>(DateTimeSerializerBaseTest.class, useTimestamp, customFormat);
        JsonGenerator gen = mock(JsonGenerator.class);
        dateTimeSerializerBase.serialize(dateTimeSerializerBaseTest, gen, serializers);
        // As this method doesn't return anything, no need to validate the result
    }

}