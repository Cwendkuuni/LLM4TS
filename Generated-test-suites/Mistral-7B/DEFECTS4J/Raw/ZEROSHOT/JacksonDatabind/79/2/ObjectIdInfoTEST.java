import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, SimpleObjectIdResolver.class);
 Assert.assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IdGenerator$, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithDefaults() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class);
 Assert.assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IdGenerator$, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdFalse() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, null);
 Assert.assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdTrue() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, true, null);
 Assert.assertTrue(objectIdInfo.getAlwaysAsId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 try {
 new ObjectIdInfo(null, Object.class, IdGenerator.class, false, null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("propertyName can not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 try {
 new ObjectIdInfo(new PropertyName("id"), null, IdGenerator.class, false, null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("scope can not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGenerator() {
 try {
 new ObjectIdInfo(new PropertyName("id"), Object.class, null, false, null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("generatorType can not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullResolver() {
 try {
 new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, null);
 new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, null);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, (Class<? extends ObjectIdResolver>) null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("resolverType can not be null", e.getMessage());
 }
 }

 @Test
 public void testObjectIdInfoWithSameProperties() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, null);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, null);
 Assert.assertEquals(objectIdInfo1, objectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithDifferentProperties() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, null);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(new PropertyName("name"), Object.class, IdGenerator.class, false, null);
 Assert.assertNotEquals(objectIdInfo1, objectIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithDifferentAlwaysAsId() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, null);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, true, null);
 Assert.assertNotEquals(objectIdInfo1, objectIdInfo2);
 }

}