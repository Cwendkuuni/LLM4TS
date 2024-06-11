import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityInfo.IdentifierGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGeneratorType;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 Class<?> clazz = MyClass.class;
 PropertyName propertyName = new PropertyName("id");
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName, clazz, IdGenerator.class);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo("id", clazz, IdGenerator.class);
 ObjectIdInfo objectIdInfo3 = new ObjectIdInfo(propertyName, clazz, IdGenerator.class, true);
 ObjectIdInfo objectIdInfo4 = new ObjectIdInfo("id", clazz, IdGenerator.class, true, ObjectIdResolver.class);

 ObjectIdInfo expectedObjectIdInfo1 = new ObjectIdInfo(propertyName, clazz, IdGenerator.class, false, ObjectIdResolver.class);
 ObjectIdInfo expectedObjectIdInfo2 = new ObjectIdInfo(new PropertyName("id"), clazz, IdGenerator.class, false);
 ObjectIdInfo expectedObjectIdInfo3 = new ObjectIdInfo(propertyName, clazz, IdGenerator.class, true, ObjectIdResolver.class);
 ObjectIdInfo expectedObjectIdInfo4 = new ObjectIdInfo(new PropertyName("id"), clazz, IdGenerator.class, true, ObjectIdResolver.class);

 assertEquals(expectedObjectIdInfo1.toString(), objectIdInfo1.toString());
 assertEquals(expectedObjectIdInfo2.toString(), objectIdInfo2.toString());
 assertEquals(expectedObjectIdInfo3.toString(), objectIdInfo3.toString());
 assertEquals(expectedObjectIdInfo4.toString(), objectIdInfo4.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), MyClass.class, IdGenerator.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

 assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, MyClass.class, IdGenerator.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullGeneratorType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), MyClass.class, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), null, IdGenerator.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), MyClass.class, IdGenerator.class, true, null);
 }

 @Test
 public void testObjectIdInfoToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), MyClass.class, IdGenerator.class);
 String toString = objectIdInfo.toString();
 String expectedString = "ObjectIdInfo: propName=id, scope=class com.example.MyClass, generatorType=class com.fasterxml.jackson.databind.BeanProperty.IdGenerator, alwaysAsId=false";
 assertEquals(expectedString, toString);
 }

 @Test
 public void testObjectIdInfoAnnotation() {
 MyClass myClass = new MyClass();
 JsonIdentityInfo jsonIdentityInfo = myClass.getClass().getAnnotation(JsonIdentityInfo.class);
 ObjectIdInfo objectIdInfo = AnnotationIntrospector.findSerializationConfig(jsonIdentityInfo).findPropertyIdInfo();

 assertEquals("id", objectIdInfo.getPropertyName().getName());
 assertEquals(MyClass.class, objectIdInfo.getScope());
 assertEquals(IdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }
}

class MyClass {
 @JsonIdentityInfo(generator = IdGenerator.class)
 private String id;
}

class IdGenerator implements IdentifierGenerator {
 @Override
 public ObjectIdGeneratorType identifierGeneratorType() {
 return ObjectIdGeneratorType.INTERNAL;
 }

 @Override
 public Object generateId(SerializerProvider serializerProvider, BeanProperty property) {
 return new Object();
 }
}