import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.IdIntGenerator.class);
 assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$IdIntGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.IdIntGenerator.class, true);
 assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$IdIntGenerator, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, String.class, ObjectIdGenerator.IdIntGenerator.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullGeneratorType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, null);
 }

 @Test
 public void testObjectIdInfoWithDefaultResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.IdIntGenerator.class);
 ObjectIdResolver resolver = SimpleObjectIdResolver.instance;
 assertEquals(objectIdInfo.getResolverType(), resolver.getClass());
 }

 @Test
 public void testObjectIdInfoWithCustomResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.IdIntGenerator.class, SimpleObjectIdResolver.class);
 ObjectIdResolver resolver = objectIdInfo.getResolverType().newInstance();
 assertNotNull(resolver);
 }

 @Test
 public void testObjectIdInfoWithDeprecatedConstructors() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.IdIntGenerator.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$IdIntGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());

 ObjectIdInfo objectIdInfoWithDeprecatedStringName = new ObjectIdInfo("name", String.class, ObjectIdGenerator.IdIntGenerator.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsIdAndDeprecatedStringName = objectIdInfoWithAlwaysAsId.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$IdIntGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsIdAndDeprecatedStringName.toString());
 }

 @Test
 public void testObjectIdInfoWithJsonIdentityInfo() {
 JsonIdentityInfo jsonIdentityInfo = new JsonIdentityInfo(PropertyName.builder().name("id").build(), String.class, ObjectIdGenerator.IntIdGenerator.class);
 ObjectIdInfo objectIdInfo = jsonIdentityInfo.getObjectIdInfo();
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$IntIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }
}