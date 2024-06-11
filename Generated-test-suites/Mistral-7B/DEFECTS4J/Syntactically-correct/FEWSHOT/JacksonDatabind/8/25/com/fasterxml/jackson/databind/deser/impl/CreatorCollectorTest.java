package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaultConstruction() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertEquals("default", instantiator.getClass().getSimpleName());
    }

    @Test
    public void testCreatorCollectorCustomConstruction() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        JavaType delegateType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty() {
            @Override
            public String getName() {
                return "injectable";
            }
            @Override
            public Object getValue(DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = new CreatorProperty() {
            @Override
            public String getName() {
                return "property";
            }
            @Override
            public Object getValue(DeserializationContext ctxt) throws IOException {
                ObjectReader reader = ctxt.getObjectReader();
                ObjectNode node = (ObjectNode) reader.readTree(new TextNode("{\"property\":\"value\"}"));
                return node.get("property").asText();
            }
        };

        collector.addDefaultCreator(null);
        collector.addStringCreator(null, true);
        collector.addIntCreator(null, true);
        collector.addLongCreator(null, true);
        collector.addDoubleCreator(null, true);
        collector.addBooleanCreator(null, true);
        collector.addDelegatingCreator(null, injectables, false);
        collector.addPropertyCreator(null, properties, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertEquals("standard", instantiator.getClass().getSimpleName());

        instantiator.configureFromObjectSettings(collector._creators[0],
                collector._creators[4], delegateType, collector._delegateArgs,
                collector._creators[6], collector._propertyBasedArgs);
        instantiator.configureFromStringCreator(collector._creators[1]);
        instantiator.configureFromIntCreator(collector._creators[3]);
        instantiator.configureFromLongCreator(collector._creators[5]);
        instantiator.configureFromDoubleCreator(collector._creators[2]);
        instantiator.configureFromBooleanCreator(collector._creators[6]);
        instantiator.configureIncompleteParameter(collector._incompleteParameter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollectorDuplicateCreator() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty() {
            @Override
            public String getName() {
                return "injectable";
            }
            @Override
            public Object getValue(DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = new CreatorProperty() {
            @Override
            public String getName() {
                return "property";
            }
            @Override
            public Object getValue(DeserializationContext ctxt) throws IOException {
                ObjectReader reader = ctxt.getObjectReader();
                ObjectNode node = (ObjectNode) reader.readTree(new TextNode("{\"property\":\"value\"}"));
                return node.get("property").asText();
            }
        };

        collector.addDefaultCreator(null);
        collector.addStringCreator(null, true);
        collector.addStringCreator(null, true); // duplicated
        collector.addIntCreator(null, true);
        collector.addLongCreator(null, true);
        collector.addDoubleCreator(null, true);
        collector.addBooleanCreator(null, true);
        collector.addBooleanCreator(null, true); // duplicated
        collector.addDelegatingCreator(null, injectables, false);
        collector.addPropertyCreator(null, properties, false);

        collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test
    public void testCreatorCollectorDeprecatedMethods() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, false);

        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = new CreatorProperty() {
            @Override
            public String getName() {
                return "injectable";
            }
            @Override
            public Object getValue(DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = new CreatorProperty() {
            @Override
            public String getName() {
                return "property";
            }
            @Override
            public Object getValue(DeserializationContext ctxt) throws IOException {
                ObjectReader reader = ctxt.getObjectReader();
                ObjectNode node = (ObjectNode) reader.readTree(new TextNode("{\"property\":\"value\"}"));
                return node.get("property").asText();
            }
        };

        collector.addDefaultCreator(null);
        collector.addStringCreator(null); // deprecated
        collector.addIntCreator(null);
        collector.addLongCreator(null);
        collector.addDoubleCreator(null);
        collector.addBooleanCreator(null);
        collector.addBooleanCreator(null); // deprecated
        collector.addDelegatingCreator(null, injectables, false);
        collector.addPropertyCreator(null, properties, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertEquals("standard", instantiator.getClass().getSimpleName());

        instantiator.configureFromObjectSettings(collector._creators[0],
                collector._creators[4], null, null,
                collector._creators[6], collector._propertyBasedArgs);
        instantiator.configureFromStringCreator(collector._creators[1]);
        instantiator.configureFromIntCreator(collector._creators[3]);
        instantiator.configureFromLongCreator(collector._creators[5]);
        instantiator.configureFromDoubleCreator(collector._creators[2]);
        instantiator.configureFromBooleanCreator(collector._creators[6]);
        instantiator.configureIncompleteParameter(collector._incompleteParameter);
    }
}