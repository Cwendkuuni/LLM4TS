package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AsPropertyTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject() {
        // Test case 1
        // p.getTypeId() returns null
        // t = JsonToken.FIELD_NAME
        // tb = null
        // expectedResult = null
        AsPropertyTypeDeserializer deser1 = new AsPropertyTypeDeserializer();
        Object result1 = deser1.deserializeTypedFromObject(p, ctxt);
        assertNull(result1);

        // Test case 2
        // p.getTypeId() returns not null
        // typeId = p.getText()
        // tbody = null
        // expectedResult = null
        AsPropertyTypeDeserializer deser2 = new AsPropertyTypeDeserializer();
        Object result2 = deser2.deserializeTypedFromObject(p, ctxt);
        assertNull(result2);
    }

    @Test
    public void testDeserializeTypedForId() {
        // Test case 1
        // typeId = p.getText()
        // _typeIdVisible = true
        // tb = null
        // deser = _findDeserializer(ctxt, typeId)
        // expectedResult = deser.deserialize(p, ctxt)
        AsPropertyTypeDeserializer deser1 = new AsPropertyTypeDeserializer();
        Object result1 = deser1.deserializeTypedForId(p, ctxt, tb);
        assertEquals(expectedResult, result1);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        // Test case 1
        // deser = _findDefaultImplDeserializer(ctxt)
        // deser = null
        // result = TypeDeserializer.deserializeIfNatural(p, ctxt, _baseType)
        // result = null
        // p.getCurrentToken() = JsonToken.START_ARRAY
        // expectedResult = super.deserializeTypedFromAny(p, ctxt)
        AsPropertyTypeDeserializer deser1 = new AsPropertyTypeDeserializer();
        Object result1 = deser1.deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        assertEquals(expectedResult, result1);

        // Test case 2
        // deser = _findDefaultImplDeserializer(ctxt)
        // deser = null
        // result = TypeDeserializer.deserializeIfNatural(p, ctxt, _baseType)
        // result = obj
        // p.getCurrentToken() = JsonToken.FIELD_NAME
        // expectedResult = obj
        AsPropertyTypeDeserializer deser2 = new AsPropertyTypeDeserializer();
        Object result2 = deser2.deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        assertEquals(expectedResult, result2);
    }

    @Test
    public void testDeserializeTypedFromAny() {
        // Test case 1
        // p.getCurrentToken() = JsonToken.START_ARRAY
        // expectedResult = super.deserializeTypedFromArray(p, ctxt)
        AsPropertyTypeDeserializer deser1 = new AsPropertyTypeDeserializer();
        Object result1 = deser1.deserializeTypedFromAny(p, ctxt);
        assertEquals(expectedResult, result1);

        // Test case 2
        // p.getCurrentToken() != JsonToken.START_ARRAY
        // expectedResult = deserializeTypedFromObject(p, ctxt)
        AsPropertyTypeDeserializer deser2 = new AsPropertyTypeDeserializer();
        Object result2 = deser2.deserializeTypedFromAny(p, ctxt);
        assertEquals(expectedResult, result2);
    }
}