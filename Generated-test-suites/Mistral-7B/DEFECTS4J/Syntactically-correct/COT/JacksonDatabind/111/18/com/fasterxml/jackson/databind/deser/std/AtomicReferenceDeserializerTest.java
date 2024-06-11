package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

private static final AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
        JsonTypeFactory.instance.constructType(AtomicReference.class),
        null, null, null);

@Test
public void testConstructor() {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 // Valid arguments
 new JavaType(AtomicReference.class),
 new ValueInstantiator() {
 // stub
 },
 new TypeDeserializer() {
 // stub
 },
 new JsonDeserializer<Object>() {
 // stub
 }
 );
 // Check instance variables
 assertNotNull(serializer.getTypeDeserializer());
 assertNull(serializer.getValueInstantiator());
 assertNull(serializer.getDeserializer());
}

@Test
public void testGetNullValue() throws JsonMappingException {
 AtomicReference<Object> actual = serializer.getNullValue(null);
 assertNotNull(actual);
 assertNull(actual.get());
}

@Test
public void testGetEmptyValue() {
 AtomicReference<Object> actual = serializer.getEmptyValue(null);
 assertNotNull(actual);
 assertNull(actual.get());
}

@Test
public void testReferenceValue() {
 Object contents = new Object();
 AtomicReference<Object> actual = serializer.referenceValue(contents);
 assertNotNull(actual);
 assertEquals(contents, actual.get());
}

@Test
public void testGetReferenced() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object actual = serializer.getReferenced(reference);
 assertEquals(reference.get(), actual);
}

@Test
public void testUpdateReference() {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = serializer.updateReference(reference, newValue);
 assertEquals(updatedReference, reference);
 assertEquals(newValue, updatedReference.get());
}

@Test(expected = UnsupportedOperationException.class)
public void testSupportsUpdateReturnsFalse() {
 assertFalse(serializer.supportsUpdate(null));
}

@Test
public void testSupportsUpdateReturnsTrue() {
 assertTrue(serializer.supportsUpdate(null));
}

@Test
public void testWithResolved() {
 TypeDeserializer typeDeserializer = null;
 JsonDeserializer<Object> valueDeserializer = null;
 AtomicReferenceDeserializer actual = serializer.withResolved(typeDeserializer, valueDeserializer);
 assertNotNull(actual);
 assertEquals(serializer.getTypeDeserializer(), actual.getTypeDeserializer());
 assertNull(actual.getValueInstantiator());
 assertNull(actual.getDeserializer());
}
}