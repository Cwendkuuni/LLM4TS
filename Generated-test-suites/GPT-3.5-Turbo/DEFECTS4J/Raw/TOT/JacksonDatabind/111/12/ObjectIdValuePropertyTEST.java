import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.Nulls;

public class ObjectIdValuePropertyTest {

   @Test
   public void testGetAnnotation() {
      ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
      PropertyMetadata metadata = new PropertyMetadata(null, null, null, false, null);

      ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
      assertNull(property.getAnnotation(Test.class));
   }

   @Test
   public void testGetMember() {
      ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
      PropertyMetadata metadata = new PropertyMetadata(null, null, null, false, null);

      ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
      assertNull(property.getMember());
   }

   @Test
   public void testDeserializeAndSet_NullValue() throws Exception {
      JsonParser p = new ObjectMapper().getFactory().createParser("null");
      DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
      Object instance = new Object();

      ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
      PropertyMetadata metadata = new PropertyMetadata(null, null, null, false, null);

      ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
      property.deserializeAndSet(p, ctxt, instance);

      assertNull(property.deserializeSetAndReturn(p, ctxt, instance));
   }

   @Test
   public void testDeserializeAndSet_NullIdProperty() throws Exception {
      JsonParser p = new ObjectMapper().getFactory().createParser("\"test\"");
      DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
      Object instance = new Object();
      JsonDeserializer<?> deser = new JsonDeserializer<>(){};
      Nulls nulls = new Nulls();
      SettableBeanProperty idProperty = new SettableBeanProperty(null, null, null, deser, null, nulls);

      ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
      objectIdReader.idProperty = null;
      PropertyMetadata metadata = new PropertyMetadata(null, null, null, false, null);

      ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

      try {
         property.deserializeAndSet(p, ctxt, instance);
         fail("UnsupportedOperationException should be thrown");
      } catch (UnsupportedOperationException e) {
         // expected exception
      }

      try {
         property.deserializeSetAndReturn(p, ctxt, instance);
         fail("UnsupportedOperationException should be thrown");
      } catch (UnsupportedOperationException e) {
         // expected exception
      }
   }

   @Test
   public void testSet_NullIdProperty() throws Exception {
      Object instance = new Object();
      Object value = new Object();

      ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
      objectIdReader.idProperty = null;
      PropertyMetadata metadata = new PropertyMetadata(null, null, null, false, null);

      ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

      try {
         property.set(instance, value);
         fail("UnsupportedOperationException should be thrown");
      } catch (UnsupportedOperationException e) {
         // expected exception
      }

      try {
         property.setAndReturn(instance, value);
         fail("UnsupportedOperationException should be thrown");
      } catch (UnsupportedOperationException e) {
         // expected exception
      }
   }

   @Test
   public void testSerialize() throws Exception {
      Object instance = new Object();
      Object value = new Object();

      ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
      PropertyMetadata metadata = new PropertyMetadata(null, null, null, false, null);

      ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

      try {
         property.serialize(instance, null, null);
         fail("UnsupportedOperationException should be thrown");
      } catch (UnsupportedOperationException e) {
         // expected exception
      }

      try {
         property.serializeAsElement(instance, null, null);
         fail("UnsupportedOperationException should be thrown");
      } catch (UnsupportedOperationException e) {
         // expected exception
      }

      try {
         property.serializeAsField(instance, null, null);
         fail("UnsupportedOperationException should be thrown");
      } catch (UnsupportedOperationException e) {
         // expected exception
      }
   }

   @Test
   public void testDeserialize() throws Exception {
      JsonParser p = new ObjectMapper().getFactory().createParser("\"test\"");
      DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
      Object instance = new Object();

      ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
      PropertyMetadata metadata = new PropertyMetadata(null, null, null, false, null);

      ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

      try {
         property.deserialize(p, ctxt, instance);
         fail("UnsupportedOperationException should be thrown");
      } catch (UnsupportedOperationException e) {
         // expected exception
      }
   }

}