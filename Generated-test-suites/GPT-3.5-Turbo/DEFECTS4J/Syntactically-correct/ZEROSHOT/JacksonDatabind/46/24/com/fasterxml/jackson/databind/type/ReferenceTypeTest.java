package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {
   
   @Test
   public void testConstruct() {
      Class<?> cls = String.class;
      JavaType refType = JavaType.STRING;
      Object valueHandler = null;
      Object typeHandler = null;
      
      ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
      
      assertEquals(cls, referenceType._class);
      assertEquals(refType.hashCode(), referenceType._hash);  
      assertEquals(valueHandler, referenceType._valueHandler);
      assertEquals(typeHandler, referenceType._typeHandler);
      assertEquals(false, referenceType._asStatic);
      assertEquals(refType, referenceType._referencedType);
   }
   
   @Test
   public void testWithTypeHandler() {
      Object h = null;
      
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      ReferenceType referenceTypeWithTypeHandler = referenceType.withTypeHandler(h);
      
      assertEquals(h, referenceTypeWithTypeHandler._typeHandler);
   }
   
   @Test
   public void testWithContentTypeHandler() {
      Object h = null;
      
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      ReferenceType referenceTypeWithContentTypeHandler = referenceType.withContentTypeHandler(h);
      
      assertEquals(h, referenceTypeWithContentTypeHandler._referencedType.getTypeHandler());
   }
   
   @Test
   public void testWithValueHandler() {
      Object h = null;
      
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      ReferenceType referenceTypeWithValueHandler = referenceType.withValueHandler(h);
      
      assertEquals(h, referenceTypeWithValueHandler._valueHandler);
   }
   
   @Test
   public void testWithContentValueHandler() {
      Object h = null;
      
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      ReferenceType referenceTypeWithContentValueHandler = referenceType.withContentValueHandler(h);
      
      assertEquals(h, referenceTypeWithContentValueHandler._referencedType.getValueHandler());
   }
   
   @Test
   public void testWithStaticTyping() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      ReferenceType referenceTypeWithStaticTyping = referenceType.withStaticTyping();
      
      assertTrue(referenceTypeWithStaticTyping._asStatic);
   }
   
   @Test
   public void testBuildCanonicalName() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      String canonicalName = referenceType.buildCanonicalName();
      
      StringBuilder sb = new StringBuilder();
      sb.append(String.class.getName());
      sb.append('<');
      sb.append(JavaType.STRING.toCanonical());
      
      assertEquals(sb.toString(), canonicalName);
   }
   
   @Test
   public void testNarrow() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      Class<?> subclass = Integer.class;
      
      ReferenceType narrowedType = (ReferenceType) referenceType._narrow(subclass);
      
      assertEquals(subclass, narrowedType._class);
   }
   
   @Test
   public void testGetReferencedType() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      JavaType referencedType = referenceType.getReferencedType();
      
      assertEquals(JavaType.STRING, referencedType);
   }
   
   @Test
   public void testIsReferenceType() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      
      assertTrue(referenceType.isReferenceType());
   }
   
   @Test
   public void testContainedTypeCount() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      
      assertEquals(1, referenceType.containedTypeCount());
   }
   
   @Test
   public void testContainedType() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      int index = 0;
      
      JavaType containedType = referenceType.containedType(index);
      
      assertEquals(JavaType.STRING, containedType);
   }
   
   @Test
   public void testContainedTypeName() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      int index = 0;
      
      String containedTypeName = referenceType.containedTypeName(index);
      
      assertEquals("T", containedTypeName);
   }
   
   @Test
   public void testGetParameterSource() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      
      Class<?> parameterSource = referenceType.getParameterSource();
      
      assertEquals(String.class, parameterSource);
   }
   
   @Test
   public void testGetErasedSignature() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      StringBuilder sb = new StringBuilder();
      
      StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
      
      assertEquals(String.class.getSimpleName(), erasedSignature.toString());
   }
   
   @Test
   public void testGetGenericSignature() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      StringBuilder sb = new StringBuilder();
      
      StringBuilder genericSignature = referenceType.getGenericSignature(sb);
      
      StringBuilder expectedSignature = new StringBuilder();
      expectedSignature.append(String.class.getSimpleName());
      expectedSignature.append('<');
      expectedSignature.append(JavaType.STRING.getGenericSignature(new StringBuilder()));
      expectedSignature.append(';');
      
      assertEquals(expectedSignature.toString(), genericSignature.toString());
   }
   
   @Test
   public void testToString() {
      ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      
      StringBuilder sb = new StringBuilder(40);
      sb.append("[reference type, class ");
      sb.append(referenceType.buildCanonicalName());
      sb.append('<');
      sb.append(JavaType.STRING);
      sb.append('>');
      sb.append(']');
      
      assertEquals(sb.toString(), referenceType.toString());
   }
   
   @Test
   public void testEquals() {
      ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      
      assertTrue(referenceType1.equals(referenceType2));
   }

   @Test
   public void testNotEquals() {
      ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
      ReferenceType referenceType2 = new ReferenceType(Integer.class, JavaType.STRING, null, null, false);
      
      assertFalse(referenceType1.equals(referenceType2));
   }
}