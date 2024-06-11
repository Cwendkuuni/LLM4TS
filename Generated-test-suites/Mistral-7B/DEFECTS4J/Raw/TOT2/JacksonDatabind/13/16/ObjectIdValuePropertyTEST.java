package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    // ...

    

    @Test
    public void testObjectIdValuePropertyConstruction_validParams() throws Exception {
        ObjectIdReader reader = new ObjectIdReader() {
            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return new StdSerializer<String>(String.class) {};
            }

            @Override
            public String getIdName() {
                return "idName";
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    @Override
                    public ObjectId generateId(JsonParser p, DeserializationContext ctxt)
                            throws IOException {
                        return null;
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromObject(Object id) {
                        return null;
                    }

                    @Override
                    public Object idFromString(String id) {
                        return null;
                    }
                };
            }
        };

        PropertyMetadata metadata = new PropertyMetadata() {};

        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, metadata);

        assertEquals("idName", property.getName());
        assertNotNull(property._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyConstruction_JsonDeserializer() throws Exception {
        ObjectIdReader reader = new ObjectIdReader() {
            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return new StdSerializer<String>(String.class) {};
            }

            // ...
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdValueProperty(reader, null),
                new StdSerializer<String>(String.class) {});

        assertEquals("idName", property.getName());
        assertNotNull(property._objectIdReader);
    }

    // ...

    
}