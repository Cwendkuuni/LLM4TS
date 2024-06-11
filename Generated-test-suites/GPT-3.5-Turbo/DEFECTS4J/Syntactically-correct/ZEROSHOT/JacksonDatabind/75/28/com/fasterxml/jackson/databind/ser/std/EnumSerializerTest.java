
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.create(), true);
        Enum<?> en = MyEnum.ONE;
        try {
            enumSerializer.serialize(en, null, null);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testSerializeAsString() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.create(), false);
        Enum<?> en = MyEnum.ONE;
        try {
            enumSerializer.serialize(en, null, null);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateContextual() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.create(), true);
        EnumSerializer contextualSerializer = (EnumSerializer) enumSerializer.createContextual(null, null);
        assertNotNull(contextualSerializer);
        assertEquals(enumSerializer.getEnumValues(), contextualSerializer.getEnumValues());
        assertEquals(enumSerializer._serializeAsIndex, contextualSerializer._serializeAsIndex);
    }

    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.create(), true);
        try {
            enumSerializer.getSchema(null, null);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.create(), true);
        try {
            enumSerializer.acceptJsonFormatVisitor(null, null);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    enum MyEnum {
        ONE, TWO, THREE
    }
}
