import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        SettableBeanProperty newProperty = property.withName(PropertyName.construct("test"));

        assertEquals(PropertyName.construct("test"), newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        SettableBeanProperty newProperty = property.withValueDeserializer(new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });

        assertNotNull(newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        SettableBeanProperty newProperty = property.withNullProvider(new ObjectMapper().getSerializationConfig().getNullValueProvider());

        assertNotNull(newProperty.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(Test.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.deserializeSetAndReturn(null, null, null));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.set(null, null);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(ObjectIdInfo.empty(), new PropertyMetadata(), new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getText();
            }
            
        });
        
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.setAndReturn(null, null);
    }
}