package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
    
public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setup() {
        BeanProperty property = OptionBuilder.withLongOpt("option")
                .withDescription("option description").hasArg().create("o");
        AnnotatedMethod setter = new AnnotatedMethod(Option.class);
        JavaType type = JavaType.builder().build();
        JsonDeserializer<Object> valueDeser = new GnuParser().getParser();
        TypeDeserializer typeDeser = new TypeDeserializer();
        KeyDeserializer keyDeser = new KeyDeserializer();
        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        String propName = "prop";
        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        Assert.assertEquals("", Option.class.getField("o").get(instance));
    }
    
    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        settableAnyProperty.deserialize(p, ctxt);
        // assert deserialized value here
    }
    
    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object propName = "propName";
        Object value = "value";
        settableAnyProperty.set(instance, propName, value);
        // assert set value here
    }
}