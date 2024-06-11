package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        boolean canFixAccess = true;
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, canFixAccess);

        assertNotNull(creatorCollector);
        assertEquals(beanDesc, creatorCollector._beanDesc);
        assertEquals(canFixAccess, creatorCollector._canFixAccess);
        assertNotNull(creatorCollector._creators);
        assertEquals(8, creatorCollector._creators.length);
        assertEquals(0, creatorCollector._explicitCreators);
        assertFalse(creatorCollector._hasNonDefaultCreator);
        assertNull(creatorCollector._delegateArgs);
        assertNull(creatorCollector._propertyBasedArgs);
        assertNull(creatorCollector._incompleteParameter);
    }

    @Test
    public void testConstructValueInstantiator() {
        DeserializationConfig config = new DeserializationConfig();
        CreatorCollector creatorCollector = new CreatorCollector();

        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);

        assertNotNull(valueInstantiator);
        assertTrue(valueInstantiator instanceof StdValueInstantiator);
        StdValueInstantiator inst = (StdValueInstantiator)valueInstantiator;
        assertEquals(config, inst.getConfig());
        assertNotNull(inst.getType());
        assertNull(inst.getDelegateCreator());
        assertNull(inst.getDelegateType());
        assertNull(inst.getDelegateArgs());
        assertNull(inst.getPropertyBasedCreator());
        assertNull(inst.getIncompleteParameter());
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.setDefaultCreator(creator);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.addStringCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.addIntCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.addLongCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.addDoubleCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.addBooleanCreator(creator, explicit);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = false;
        CreatorProperty[] injectables = new CreatorProperty[0];
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.addDelegatingCreator(creator, explicit, injectables);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        boolean explicit = false;
        CreatorProperty[] properties = new CreatorProperty[0];
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.addPropertyCreator(creator, explicit, properties);

        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.addIncompeteParameter(parameter);

        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector();

        assertFalse(creatorCollector.hasDefaultCreator());

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);

        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void test_fixAccess() {
        Member member = new Member();
        CreatorCollector creatorCollector = new CreatorCollector();

        Member fixedMember = creatorCollector._fixAccess(member);

        assertNotNull(fixedMember);
    }

    @Test
    public void testVerifyNonDup() {
        AnnotatedWithParams newOne = new AnnotatedWithParams();
        int typeIndex = CreatorCollector.C_DEFAULT;
        CreatorCollector creatorCollector = new CreatorCollector();

        creatorCollector.verifyNonDup(newOne, typeIndex);

        assertEquals(newOne, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
}