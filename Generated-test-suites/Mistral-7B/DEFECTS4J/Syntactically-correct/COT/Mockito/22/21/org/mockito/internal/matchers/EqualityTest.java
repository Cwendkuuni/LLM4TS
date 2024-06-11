import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreArraysEqualMode;

public class EqualityTest {

 @Test
 public void testAreEqual_Null_Null() {
 ArbitraryObjects arb = new ArbitraryObjects();
 Object o1 = arb.randomObject();
 Object o2 = arb.randomObject();

 assertTrue(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_Null_NotNull() {
 Object o1 = null;
 Object o2 = new Object();

 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_NotNull_Null() {
 Object o1 = new Object();
 Object o2 = null;

 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_NotEqualObjects() {
 ArbitraryObjects arb = new ArbitraryObjects();
 Object o1 = arb.randomObject();
 Object o2 = arb.randomObject();

 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_EqualObjects() {
 ArbitraryObjects arb = new ArbitraryObjects();
 Object o1 = arb.randomObject();
 Object o2 = o1;

 assertTrue(Equality.areEqual(o1, o2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqual_NotObjects() {
 int i1 = 1;
 int i2 = 2;

 try {
 Equality.areEqual(i1, i2);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test
 public void testAreArraysEqual_NullArrays() {
 Object[] o1 = null;
 Object[] o2 = null;

 assertTrue(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_SameArrays() {
 Object[] o1 = new Object[] { new Object(), new Object() };
 Object[] o2 = o1;

 assertTrue(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_DifferentArraysLength() {
 Object[] o1 = new Object[] { new Object(), new Object() };
 Object[] o2 = new Object[] { new Object() };

 assertFalse(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_DifferentArraysContents() {
 Object[] o1 = new Object[] { new Object(), new Object() };
 Object[] o2 = new Object[] { new Object(), new Object(), new Object() };

 assertFalse(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_DifferentArraysSameElementTypes() {
 Object[] o1 = new Object[] { 1, "two" };
 Object[] o2 = new Object[] { 1, 2 };

 assertFalse(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_DifferentArraysSameElements() {
 Object[] o1 = new Object[] { new Object(), new Object() };
 Object[] o2 = new Object[] { new Object(), new Object() };

 assertTrue(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_ArraysWithDifferentTypes_Mode1() {
 Object[] o1 = new Object[] { 1, "two" };
 Object[] o2 = new Object[] { 1, new Object() };

 AreArraysEqualMode mode1 = new AreArraysEqualMode(true, true);
 assertTrue(Equality.areArraysEqual(o1, o2, mode1));
 }

 @Test
 public void testAreArraysEqual_ArraysWithDifferentTypes_Mode2() {
 Object[] o1 = new Object[] { 1, "two" };
 Object[] o2 = new Object[] { 1, new Object() };

 AreArraysEqualMode mode2 = new AreArraysEqualMode(false, true);
 assertFalse(Equality.areArraysEqual(o1, o2, mode2));
 }

 @Test
 public void testAreArraysEqual_ArraysWithSameTypes_Mode1() {
 Object[] o1 = new Object[] { 1, "two" };
 Object[] o2 = new Object[] { 1, "two" };

 AreArraysEqualMode mode1 = new AreArraysEqualMode(true, true);
 assertTrue(Equality.areArraysEqual(o1, o2, mode1));
 }

 @Test
 public void testAreArraysEqual_ArraysWithSameTypes_Mode2() {
 Object[] o1 = new Object[] { 1, "two" };
 Object[] o2 = new Object[] { 1, "two" };

 AreArraysEqualMode mode2 = new AreArraysEqualMode(false, true);
 assertTrue(Equality.areArraysEqual(o1, o2, mode2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreArraysEqual_ObjectsAndArrays() {
 Object o1 = new Object();
 Object[] o2 = new Object[] { new Object() };

 try {
 Equality.areArraysEqual(o1, o2);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
}

 private static class ArbitraryObjects {
 Object randomObject() {
 return Math.random() > 0.5 ? new Object() {} : new int[10];
 }
 }

 static class AreArraysEqualMode {
 boolean arraysArePrimitive;
 boolean ignoreFirstDifference;

 public AreArraysEqualMode(boolean arraysArePrimitive, boolean ignoreFirstDifference) {
 this.arraysArePrimitive = arraysArePrimitive;
 this.ignoreFirstDifference = ignoreFirstDifference;
 }
 }
}