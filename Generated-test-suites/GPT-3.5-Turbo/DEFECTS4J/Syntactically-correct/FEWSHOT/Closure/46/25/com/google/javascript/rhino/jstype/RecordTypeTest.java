package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
 
   private JSTypeRegistry registry;
   private RecordType recordType;
  
   @Before
   public void setUp() {
       registry = new JSTypeRegistry();
       HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
       properties.put("property1", new RecordTypeBuilder.RecordProperty(new JSType(registry), null));
       properties.put("property2", new RecordTypeBuilder.RecordProperty(new JSType(registry), null));
       recordType = new RecordType(registry, properties);
   }
  
   @Test
   public void testIsEquivalentTo() {
       assertTrue(recordType.isEquivalentTo(new RecordType(registry, recordType.properties)));
      
       HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
       properties.put("property1", new RecordTypeBuilder.RecordProperty(new JSType(registry), null));
       assertFalse(recordType.isEquivalentTo(new RecordType(registry, properties)));
   }
  
   @Test
   public void testGetImplicitPrototype() {
       JSType object = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
       assertEquals(object, recordType.getImplicitPrototype());
   }
  
   @Test
   public void testDefineProperty() {
       assertFalse(recordType.defineProperty("property3", new JSType(registry), false, null));
       assertFalse(recordType.defineProperty("property1", new JSType(registry), false, null));
   }
  
   @Test
   public void testGetLeastSupertype() {
       assertEquals(recordType, recordType.getLeastSupertype(recordType));
      
       HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
       properties.put("property1", new RecordTypeBuilder.RecordProperty(new JSType(registry), null));
       RecordType otherType = new RecordType(registry, properties);
      
       assertEquals(otherType, recordType.getLeastSupertype(otherType));
      
       properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
       properties.put("property3", new RecordTypeBuilder.RecordProperty(new JSType(registry), null));
       otherType = new RecordType(registry, properties);
      
       assertNull(recordType.getLeastSupertype(otherType));
   }
  
   @Test
   public void testIsSubtype() {
       assertTrue(recordType.isSubtype(recordType));
      
       HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
       properties.put("property1", new RecordTypeBuilder.RecordProperty(new JSType(registry), null));
       assertTrue(recordType.isSubtype(new RecordType(registry, properties)));
      
       properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();
       properties.put("property3", new RecordTypeBuilder.RecordProperty(new JSType(registry), null));
       assertFalse(recordType.isSubtype(new RecordType(registry, properties)));
   }
  
   @Test
   public void testResolveInternal() {
       assertNull(recordType.resolveInternal(null, null));
   }
  
   @Test
   public void testToMaybeRecordType() {
       assertEquals(recordType, recordType.toMaybeRecordType());
   }
}