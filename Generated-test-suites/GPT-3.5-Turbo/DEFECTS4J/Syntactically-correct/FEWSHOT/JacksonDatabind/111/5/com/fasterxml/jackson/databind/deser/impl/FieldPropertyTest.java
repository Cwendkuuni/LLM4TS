package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {

    private FieldProperty fieldProperty;
    private Field annotatedField;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;

    @Before
    public void setUp() {
        annotatedField = new Field();
        propDef = new BeanPropertyDefinition();
        type = new JavaType();
        typeDeser = new TypeDeserializer();
        contextAnnotations = new Annotations();
        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, annotateField);
    }

    @Test
    public void testConstructor() {
        assertNull(fieldProperty.withName(null));
        assertEquals(fieldProperty.withValueDeserializer(null), fieldProperty);
        assertNull(fieldProperty.withNullProvider(null));
    }

    @Test
    public void testFixAccess() {
        fieldProperty.fixAccess(new DeserializationConfig());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(fieldProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertEquals(fieldProperty.getMember(), annotatedField);
    }

    @Test
    public void testDeserializeAndSet() {}

    @Test
    public void testDeserializeSetAndReturn() {}

    @Test
    public void testSet() {}

    @Test
    public void testSetAndReturn() {}

    @Test
    public void testReadResolve() {
        assertEquals(fieldProperty.readResolve(), new FieldProperty(this));
    }
}