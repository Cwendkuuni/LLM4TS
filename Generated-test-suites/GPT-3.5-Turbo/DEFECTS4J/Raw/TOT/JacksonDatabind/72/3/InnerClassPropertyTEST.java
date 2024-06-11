import static org.junit.Assert.*;

import org.junit.Test;

public class InnerClassPropertyTest {

   @Test
   public void testConstructor() {
      SettableBeanProperty delegate = new SettableBeanPropertyImpl();
      Constructor<?> ctor = new ConstructorImpl();
      InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
      
      assertEquals(delegate, innerClassProperty._delegate);
      assertEquals(ctor, innerClassProperty._creator);
   }

   @Test
   public void testConstructorWithAnnotatedConstructor() {
      SettableBeanProperty delegate = new SettableBeanPropertyImpl();
      Constructor<?> ctor = new ConstructorImpl();
      AnnotatedConstructor ann = new AnnotatedConstructorImpl();
      InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ann);
      
      assertEquals(delegate, innerClassProperty._delegate);
      assertEquals(ann, innerClassProperty._annotated);
      assertEquals(ctor, innerClassProperty._creator);
   }

   @Test
   public void testConstructorWithJsonDeserializer() {
      SettableBeanProperty delegate = new SettableBeanPropertyImpl();
      Constructor<?> ctor = new ConstructorImpl();
      JsonDeserializer<?> deser = new JsonDeserializerImpl();
      InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, deser);
      
      assertEquals(delegate.withValueDeserializer(deser), innerClassProperty._delegate);
      assertEquals(ctor, innerClassProperty._creator);
   }

   @Test
   public void testConstructorWithPropertyName() {
      SettableBeanProperty delegate = new SettableBeanPropertyImpl();
      Constructor<?> ctor = new ConstructorImpl();
      PropertyName newName = new PropertyName("newName");
      InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, newName);
      
      assertEquals(delegate.withName(newName), innerClassProperty._delegate);
      assertEquals(ctor, innerClassProperty._creator);
   }
   
   @Test
   public void testWithName() {
      SettableBeanProperty delegate = new SettableBeanPropertyImpl();
      Constructor<?> ctor = new ConstructorImpl();
      PropertyName newName = new PropertyName("newName");
      InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
      
      InnerClassProperty property = innerClassProperty.withName(newName);
      
      assertEquals(delegate.withName(newName), property._delegate);
      assertEquals(ctor, property._creator);
   }

   @Test
   public void testWithValueDeserializer() {
      SettableBeanProperty delegate = new SettableBeanPropertyImpl();
      Constructor<?> ctor = new ConstructorImpl();
      JsonDeserializer<?> deser = new JsonDeserializerImpl();
      InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
      
      InnerClassProperty property = innerClassProperty.withValueDeserializer(deser);
      
      assertEquals(delegate.withValueDeserializer(deser), property._delegate);
      assertEquals(ctor, property._creator);
   }

   @Test
   public void testAssignIndex() {
      SettableBeanProperty delegate = new SettableBeanPropertyImpl();
      Constructor<?> ctor = new ConstructorImpl();
      InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
      
      innerClassProperty.assignIndex(2);
      
      assertEquals(2, delegate.getPropertyIndex());
   }
   
   // ... more test methods for other methods

}