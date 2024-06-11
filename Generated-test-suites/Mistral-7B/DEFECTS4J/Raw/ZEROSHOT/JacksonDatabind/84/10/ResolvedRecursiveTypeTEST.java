import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;

@RunWith(org.mockito.jUnit.MockitoJUnitRunner.class)
public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = Mockito.mock(JavaType.class);
 selfType.setReference(refType);
 JavaType expectedSelfType = new ResolvedRecursiveType(Object.class, null);
 expectedSelfType.setReference(refType);
 assertEquals(expectedSelfType, selfType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = Mockito.mock(JavaType.class);
 selfType.setReference(refType);
 selfType.setReference(refType);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = Mockito.mock(JavaType.class);
 selfType.setReference(refType);
 assertEquals(refType, selfType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(List.class, null);
 StringBuilder expectedSb = new StringBuilder("[recursive type; ");
 StringBuilder actualSb = new StringBuilder();
 selfType.getGenericSignature(actualSb);
 assertEquals(expectedSb.toString(), actualSb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(List.class, null);
 StringBuilder expectedSb = new StringBuilder("[Ljava.util.List;");
 StringBuilder actualSb = new StringBuilder();
 selfType.getErasedSignature(actualSb);
 assertEquals(expectedSb.toString(), actualSb.toString());
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(Object.class, null);
 assertFalse(selfType.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(List.class, null);
 JavaType refType = Mockito.mock(JavaType.class);
 selfType.setReference(refType);
 String expectedString = "[recursive type; " + refType.getRawClass().getName() + "]";
 String actualString = selfType.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType selfType1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType selfType2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(selfType1.equals(selfType2));

 ResolvedRecursiveType selfType3 = new ResolvedRecursiveType(Object.class, null);
 selfType3.setReference(selfType1);
 assertTrue(selfType1.equals(selfType3));
 }

 @Test
 public void testEqualsWithNull() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(Object.class, null);
 assertFalse(selfType.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClass() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(Object.class, null);
 JavaType otherType = Mockito.mock(JavaType.class);
 assertFalse(selfType.equals(otherType));
 }

 @Test
 public void testEqualsWithUnresolvedReference() {
 ResolvedRecursiveType selfType = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType otherType = new ResolvedRecursiveType(Object.class, null);
 assertFalse(selfType.equals(otherType));
 }

 @Test
 public void testEqualsWithSelfReference() {
 ResolvedRecursiveType selfType1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType selfType2 = new ResolvedRecursiveType(Object.class, null);
 selfType2.setReference(selfType1);
 assertTrue(selfType1.equals(selfType2));
 }
}