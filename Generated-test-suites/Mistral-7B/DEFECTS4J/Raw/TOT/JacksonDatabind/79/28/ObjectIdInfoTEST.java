import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstructor1() {
 ObjectIdInfo objIdInfo1 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, ObjectIdResolver.class);
 ObjectIdInfo objIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class);
 ObjectIdInfo objIdInfo3 = new ObjectIdInfo("testProp", Object.class, ObjectIdGenerator.class);

 assertEquals(objIdInfo1, objIdInfo2);
 assertEquals(objIdInfo1.toString(), "ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false");
 assertEquals(objIdInfo3.toString(), "ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false");
 }

 @Test
 public void testObjectIdInfoConstructor2() {
 ObjectIdInfo objIdInfo1 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, ObjectIdResolver.class);
 ObjectIdInfo objIdInfo2 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objIdInfo3 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class);

 assertNotEquals(objIdInfo1, objIdInfo3);
 assertEquals(objIdInfo1.toString(), "ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false");
 assertEquals(objIdInfo2.toString(), "ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false");
 }

 @Test
 public void testObjectIdInfoConstructor3() {
 ObjectIdInfo objIdInfo1 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objIdInfo2 = new ObjectIdInfo("testProp", Object.class, ObjectIdGenerator.class, false);

 assertEquals(objIdInfo1, objIdInfo2);
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objIdInfo1 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objIdInfo2 = objIdInfo1.withAlwaysAsId(true);

 assertNotEquals(objIdInfo1, objIdInfo2);
 assertEquals(objIdInfo2.getAlwaysAsId(), true);
 }

 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoNullResolver() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false, null);
 }

 @Test
 public void testObjectIdInfoToString() {
 ObjectIdInfo objIdInfo1 = new ObjectIdInfo(new PropertyName("testProp"), Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objIdInfo2 = new ObjectIdInfo(new PropertyName("testProp2"), Object.class, ObjectIdGenerator.class, false);

 assertEquals(objIdInfo1.toString(), "ObjectIdInfo: propName=testProp, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false");
 assertEquals(objIdInfo2.toString(), "ObjectIdInfo: propName=testProp2, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false");
 }
}