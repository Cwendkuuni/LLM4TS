import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, ObjectIdResolver.class);

 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameStringAndScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class);

 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoConstructionWithPropertyNameStringAndScopeAndAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, true, ObjectIdResolver.class);

 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo originalObjectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class);
 ObjectIdInfo newObjectIdInfo = originalObjectIdInfo.withAlwaysAsId(true);

 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true", newObjectIdInfo.toString());
 assertNotSame(originalObjectIdInfo, newObjectIdInfo);
 }

 @Test
 public void testObjectIdInfoEquals() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class);

 assertTrue(objectIdInfo1.equals(objectIdInfo2));
 }

 @Test
 public void testObjectIdInfoNotEqualsDifferentPropertyName() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(new PropertyName("testPropertyName1"), Object.class, ObjectIdGenerator.class);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(new PropertyName("testPropertyName2"), Object.class, ObjectIdGenerator.class);

 assertFalse(objectIdInfo1.equals(objectIdInfo2));
 }

 @Test
 public void testObjectIdInfoNotEqualsDifferentScope() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(new PropertyName("testPropertyName"), String.class, ObjectIdGenerator.class);

 assertFalse(objectIdInfo1.equals(objectIdInfo2));
 }

 @Test
 public void testObjectIdInfoNotEqualsDifferentGeneratorType() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdResolver.class);

 assertFalse(objectIdInfo1.equals(objectIdInfo2));
 }

 @Test
 public void testObjectIdInfoNotEqualsDifferentAlwaysAsId() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, true);

 assertFalse(objectIdInfo1.equals(objectIdInfo2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, false);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), null, ObjectIdGenerator.class, false);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullGeneratorType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, null, false);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.class, false, null);
 fail("Should throw IllegalArgumentException");
 }

}