package com.google.gson.internal.test;

import static org.junit.Assert.*;

import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_ValidClass() throws Exception {
    Class<String> stringClass = String.class;
    String instance = (String) unsafeAllocator.newInstance(stringClass);
    assertNotNull(instance);
    assertEquals("", instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_InterfaceClass() throws Exception {
    Class<Serializable> serializableClass = Serializable.class;
    unsafeAllocator.newInstance(serializableClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() throws Exception {
    Class<AbstractClass> abstractClass = AbstractClass.class;
    unsafeAllocator.newInstance(abstractClass);
  }

  @Test
  public void testNewInstance_NullClass() throws Exception {
    Class<?> nullClass = null;
    try {
      unsafeAllocator.newInstance(nullClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testNewInstance_PrimativeTypes() throws Exception {
    Class<Byte> byteClass = Byte.class;
    Byte instance = (Byte) unsafeAllocator.newInstance(byteClass);
    assertNotNull(instance);
    assertEquals((byte) 0, instance);

    Class<Boolean> booleanClass = Boolean.class;
    Boolean instance2 = (Boolean) unsafeAllocator.newInstance(booleanClass);
    assertNotNull(instance2);
    assertEquals(false, instance2);

    Class<Short> shortClass = Short.class;
    Short instance3 = (Short) unsafeAllocator.newInstance(shortClass);
    assertNotNull(instance3);
    assertEquals((short) 0, instance3);

    Class<Integer> intClass = Integer.class;
    Integer instance4 = (Integer) unsafeAllocator.newInstance(intClass);
    assertNotNull(instance4);
    assertEquals(0, instance4);

    Class<Long> longClass = Long.class;
    Long instance5 = (Long) unsafeAllocator.newInstance(longClass);
    assertNotNull(instance5);
    assertEquals(0L, instance5);

    Class<Float> floatClass = Float.class;
    Float instance6 = (Float) unsafeAllocator.newInstance(floatClass);
    assertNotNull(instance6);
    assertEquals(0F, instance6, 0.0f);

    Class<Double> doubleClass = Double.class;
    Double instance7 = (Double) unsafeAllocator.newInstance(doubleClass);
    assertNotNull(instance7);
    assertEquals(0D, instance7, 0.0d);

    Class<Character> charClass = Character.class;
    Character instance8 = (Character) unsafeAllocator.newInstance(charClass);
    assertNotNull(instance8);
    assertEquals('\u0000', instance8);
  }

  @Test
  public void testNewInstance_ArrayClass() throws Exception {
    Class<Byte[]> byteArrayClass = Byte[].class;
    Byte[] instance = (Byte[]) unsafeAllocator.newInstance(byteArrayClass);
    assertNotNull(instance);
    assertEquals(0, instance.length);
  }

  @Test
  public void testNewInstance_ClassWithPrivateConstructor() throws Exception {
    Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;
    PrivateConstructorClass instance = (PrivateConstructorClass) unsafeAllocator.newInstance(privateConstructorClass);
    assertNotNull(instance);
  }

  @Test
  public void testNewInstance_ClassWithCustomAllocator() throws Exception {
    Class<CustomAllocatorClass> customAllocatorClass = CustomAllocatorClass.class;
    CustomAllocatorClass instance = (CustomAllocatorClass) unsafeAllocator.newInstance(customAllocatorClass);
    assertNotNull(instance);
  }

  @Test
  public void testNewInstance_SerializationTest() throws Exception {
    Class<SerializationTestClass> serializationTestClass = SerializationTestClass.class;
    SerializationTestClass instance = (SerializationTestClass) unsafeAllocator.newInstance(serializationTestClass);
    assertNotNull(instance);
    assertEquals("test", instance.str);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    objectOutputStream.writeObject(instance);
    objectOutputStream.close();

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    SerializationTestClass deserializedInstance = (SerializationTestClass) objectInputStream.readObject();
    objectInputStream.close();

    assertEquals("test", deserializedInstance.str);
  }

  @Test
  public void testCreate_MultipleMethods() throws Exception {
    assertEquals(4, UnsafeAllocator.class.getDeclaredMethods().length);
  }

  @Test
  public void testCreate_ReturnsInstanceOfUnsafeAllocatorImpl() throws Exception {
    UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
    assertTrue(unsafeAllocator instanceof UnsafeAllocatorImpl);
  }

  public static class AbstractClass {
  }

  public static class PrivateConstructorClass {
    private PrivateConstructorClass() {
    }
  }

  public static class CustomAllocatorClass {
    static CustomAllocatorClass allocator = new CustomAllocatorClass();

    private CustomAllocatorClass() {
    }

    public static CustomAllocatorClass getInstance() {
      return allocator;
    }
  }

  public static class SerializationTestClass implements Serializable {
    String str;

    public SerializationTestClass(String str) {
      this.str = str;
    }
  }
}