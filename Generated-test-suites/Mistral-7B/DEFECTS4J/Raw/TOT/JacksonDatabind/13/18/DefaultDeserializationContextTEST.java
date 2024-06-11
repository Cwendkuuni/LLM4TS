package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIdentityInfo(generator = ObjectIdGenerator.PropertyGenerator.class, property = "id")
public class DefaultDeserializationContextTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private DefaultDeserializationContext context;

    @BeforeMethod
    public void setUp() throws JsonMappingException {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDefaultPropertyInclusion(findJsonProperty(JsonProperty.Access.FIELD));
        reader = mapper.readerForListOf(TestBean.class);
        context = mapper.getDeserializationContext();
    }

    @Test
    public void testSimpleDeserialization() throws IOException {
        TestBean[] beans = reader.readValue(new StringReader("[[1, 'a'], [2, 'b']]"));
        Assert.assertEquals(beans.length, 2);
        Assert.assertEquals(beans[0].getId(), "1");
        Assert.assertEquals(beans[1].getId(), "2");
    }

    @Test
    public void testDeserializationWithResolver() throws IOException {
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context = new DefaultDeserializationContext(context, resolver);
        TestBean[] beans = reader.readValue(new StringReader("[[1, 'a'], [2, 'b']]"), context);
        Assert.assertEquals(beans.length, 2);
        Assert.assertEquals(beans[0].getId(), "1");
        Assert.assertEquals(beans[1].getId(), "2");
    }

    @Test
    public void testDeserializationWithFactory() throws IOException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(mapper.getDeserializationConfig(), null, null, null);
        TestBean[] beans = reader.readValue(new StringReader("[[1, 'a'], [2, 'b']]"), context);
        Assert.assertEquals(beans.length, 2);
        Assert.assertEquals(beans[0].getId(), "1");
        Assert.assertEquals(beans[1].getId(), "2");
    }

    @Test(expectedExceptions = UnresolvedForwardReference.class)
    public void testUnresolvedObjectId() throws IOException {
        context = mapper.getDeserializationContext();
        String json = "{\"id\": 1, \"ref\": {\"$ref\": \"#/properties/refs[0]\", \"name\": \"ref1\"}}";
        TestBeanRef bean = mapper.readValue(new StringReader(json), TestBeanRef.class, context);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializationWithCustomHandler() throws IOException {
        final ObjectIdGenerator idGenerator = new StdSerializer<ObjectIdGenerator>(ObjectIdGenerator.class) {
            @Override
            public ObjectIdGenerator createContextualObjectIdGenerator(DeserializationContext ctxt) {
                return new ObjectIdGenerator() {
                    private int counter = 0;

                    @Override
                    public ObjectIdGenerator.IdKey key(Object id) {
                        return new ObjectIdGenerator.IdKey() {
                            @Override
                            public String id() {
                                return Integer.toString(counter++);
                            }
                        };
                    }
                };
            }
        };
        context = new DefaultDeserializationContext(mapper.getDeserializationConfig(), idGenerator);
        TestBean[] beans = reader.readValue(new StringReader("[[1, 'a'], [2, 'b']]"), context);
        Assert.assertEquals(beans.length, 2);
        Assert.assertEquals(beans[0].getId(), "0");
        Assert.assertEquals(beans[1].getId(), "1");
    }

    @Test
    public void testDeserializationWithCustomHandlerAndResolver() throws IOException {
        ObjectIdGenerator idGenerator = new StdSerializer<ObjectIdGenerator>(ObjectIdGenerator.class) {
            @Override
            public ObjectIdGenerator createContextualObjectIdGenerator(DeserializationContext ctxt) {
                return new ObjectIdGenerator() {
                    private int counter = 0;

                    @Override
                    public ObjectIdGenerator.IdKey key(Object id) {
                        return new ObjectIdGenerator.IdKey() {
                            @Override
                            public String id() {
                                return Integer.toString(counter++);
                            }
                        };
                    }
                };
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context = new DefaultDeserializationContext(context, idGenerator, resolver);
        TestBean[] beans = reader.readValue(new StringReader("[[1, 'a'], [2, 'b']]"), context);
        Assert.assertEquals(beans.length, 2);
        Assert.assertEquals(beans[0].getId(), "0");
        Assert.assertEquals(beans[1].getId(), "1");
    }

    private static JsonProperty findJsonProperty(JsonProperty.Access access) {
        return new JsonProperty() {
            @Override
            public JsonProperty.Access getAccess() {
                return access;
            }
        };
    }

    @JsonIdentityInfo(generator = ObjectIdGenerator.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    static class TestBean {
        @JsonProperty("id")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static class TestBeanRef {
        @JsonProperty("$ref")
        private String ref;

        @JsonProperty("name")
        private String name;

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}