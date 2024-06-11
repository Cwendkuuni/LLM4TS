import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectIdInfoTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objIdInfo1 = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false, ObjectIdResolver.class);
 ObjectIdInfo objIdInfo2 = new ObjectIdInfo(new PropertyName("name"), String.class, IdGenerator.class, false);
 ObjectIdInfo objIdInfo3 = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, true, ObjectIdResolver.class);

 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IdGenerator, alwaysAsId=false", objIdInfo1.toString());
 assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.jsontype.IdGenerator, alwaysAsId=false", objIdInfo2.toString());
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IdGenerator, alwaysAsId=true", objIdInfo3.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class, IdGenerator.class, false);
 ObjectIdInfo withAlwaysAsId = objIdInfo.withAlwaysAsId(true);

 assertNotSame(objIdInfo, withAlwaysAsId);
 assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.jsontype.IdGenerator, alwaysAsId=true", withAlwaysAsId.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoNullArguments() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(null, Object.class, IdGenerator.class, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoInvalidScopeType() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("id"), int.class, IdGenerator.class, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullPropertyName() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(null, Object.class, IdGenerator.class, false);
 }

}