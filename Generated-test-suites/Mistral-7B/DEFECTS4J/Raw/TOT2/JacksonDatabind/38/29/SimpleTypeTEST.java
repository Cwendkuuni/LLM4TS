package com.example.junit;

import static org.junit.Assert.*;

import org.junit.*;

public class SimpleTypeTest {

@Test
public void testConstruct() {
Class<String> stringClass = String.class;
SimpleType simpleType = SimpleType.construct(stringClass);
assertEquals("class java.lang.String", simpleType.getCanonicalName());
}

@Test
public void testConstructNull() {
assertNull(SimpleType.construct(null));
}

@Test(expected = IllegalArgumentException.class)
public void testConstructNonClass() {
SimpleType.construct(new Object());
}

@Test(expected = IllegalArgumentException.class)
public void testConstructArrayType() {
int[] intArray = new int[5];
assertNull(SimpleType.construct(intArray.getClass()));
}

@Test(expected = IllegalArgumentException.class)
public void testConstructCollectionType() {
List<String> list = new ArrayList<>();
assertNull(SimpleType.construct(list.getClass()));
}

@Test(expected = IllegalArgumentException.class)
public void testConstructFinalClass() {
final class FinalClass {}
assertNull(SimpleType.construct(FinalClass.class));
}
}