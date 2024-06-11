package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private static class TestSerializerProvider extends SerializerProvider {

        protected TestSerializerProvider() {
            super(new ObjectMapper(), new SerializationConfig(), null, new DefaultSerializerFactory(), null);
        }

        @Override
        public JsonGenerator getGenerator() {
            return null;
        }

        @Override
        public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> findValueSerializer(JavaType valueType, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> serializerInstance(Annotated annotated, Object serDef) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> serializerInstance(Class<?> serClass, Object serDef) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> keySerializerInstance(Annotated annotated, Object serDef) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> keySerializerInstance(Class<?> serClass, Object serDef) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> getDefaultNullValueSerializer() {
            return null;
        }

        @Override
        public JsonSerializer<Object> findTypedValueSerializer(Class<?> valueType, boolean cache, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> findTypedValueSerializer(JavaType valueType, boolean cache, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findTypedValueDeserializer(JavaType valueType, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findTypedValueDeserializer(Class<?> valueType, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> findKeySerializer(Class<?> keyType, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonSerializer<Object> getUnknownTypeSerializer(Class<?> unknownType) {
            return null;
        }

        @Override
        public boolean hasSerializerFor(Class<?> cls, AtomicReference<Throwable> cause) {
            return false;
        }

        @Override
        public DateFormat getDateFormat() {
            return null;
        }

        @Override
        public TimeZone getTimeZone() {
            return null;
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
            return null;
        }

        @Override
        public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType, JsonProperty.Access acc) {
            return null;
        }

        @Override
        public JsonSerializer<Object> getNullValueSerializer() {
            return null;
        }

        @Override
        public JsonSerializer<Object> getNullKeySerializer() {
            return null;
        }

        @Override
        public JsonSerializer<Object> findNonContextualValueSerializer(JavaType valueType, BeanProperty property) throws JsonMappingException {
            return null;
        }
    }

    private static class TestDateTimeSerializerBase extends DateTimeSerializerBase<Date> {

        protected TestDateTimeSerializerBase(Boolean useTimestamp, DateFormat customFormat) {
            super(Date.class, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        }

    }

    public static void main(String[] args) {
        TestDateTimeSerializerBase serializer = new TestDateTimeSerializerBase(true, null);

        try {
            BeanProperty property = null;
            SerializerProvider serializers = new TestSerializerProvider();
            serializer.createContextual(serializers, property);
            serializer.getSchema(serializers, Date.class);
            serializer.acceptJsonFormatVisitor(null, null);
            serializer.serialize(null, null, serializers);
        } catch (JsonMappingException | IOException e) {
            e.printStackTrace();
        }
    }
}