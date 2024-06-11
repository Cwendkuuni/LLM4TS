package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DatabindContextTest {

    @Test
    public void testConstructType() {
        DatabindContext context = new DatabindContextImpl();
        assertNull(context.constructType(null));
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = new DatabindContextImpl();
        assertNull(context.constructSpecializedType(null, null));
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = new DatabindContextImpl();
        JavaType baseType = new JavaType();
        String subClass = "SubClass";
        try {
            context.resolveSubType(baseType, subClass);
        } catch (JsonMappingException e) {
            assertEquals("Not a subtype", e.getMessage());
        }
    }

    @Test
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = new DatabindContextImpl();
        JavaType baseType = new JavaType();
        String typeId = "TypeId";
        String extraDesc = "ExtraDesc";
        try {
            context.invalidTypeIdException(baseType, typeId, extraDesc);
        } catch (JsonMappingException e) {
            assertEquals("Not a subtype", e.getMessage());
        }
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = new DatabindContextImpl();
        assertNull(context.converterInstance(null, null));
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = new DatabindContextImpl();
        JavaType type = new JavaType();
        String msg = "BadDefinition";
        try {
            context.reportBadDefinition(type, msg);
        } catch (JsonMappingException e) {
            assertEquals("BadDefinition", e.getMessage());
        }
    }

    @Test
    public void testReportBadDefinitionWithClass() throws JsonMappingException {
        DatabindContext context = new DatabindContextImpl();
        Class<?> type = String.class;
        String msg = "BadDefinition";
        try {
            context.reportBadDefinition(type, msg);
        } catch (JsonMappingException e) {
            assertEquals("BadDefinition", e.getMessage());
        }
    }
}