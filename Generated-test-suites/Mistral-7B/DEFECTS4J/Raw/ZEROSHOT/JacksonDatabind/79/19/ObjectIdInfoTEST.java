import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false, ObjectIdResolver.class);

 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdTrue() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, true, ObjectIdResolver.class);

 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdFalse() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false, ObjectIdResolver.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsIdTrue = objectIdInfo.withAlwaysAsId(true);

 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsIdTrue.toString());
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, false, ObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null, ObjectIdGenerator.class, false, ObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGeneratorType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, false, ObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false, null);
 }
}