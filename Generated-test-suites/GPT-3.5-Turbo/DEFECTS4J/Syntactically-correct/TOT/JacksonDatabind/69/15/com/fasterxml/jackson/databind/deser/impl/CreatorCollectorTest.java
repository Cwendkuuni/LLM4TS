
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        creatorCollector.addIntCreator(creator, true);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, true);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, true);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedWithParams creator = new TestAnnotatedWithParams();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        AnnotatedParameter parameter = new TestAnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertNotNull(creatorCollector._incompleteParameter);
    }

    class TestAnnotatedWithParams extends AnnotatedWithParams {

        @Override
        public Class<?> getRawParameterType(int index) {
            return null;
        }

        @Override
        public JavaType getParameterType(int index) {
            return null;
        }

        @Override
        public TypeResolver getTypeResolver() {
            return null;
        }

        @Override
        public Object call() throws Exception {
            return null;
        }

        @Override
        public Object call(Object[] args) throws Exception {
            return null;
        }

        @Override
        public Object call1(Object arg) throws Exception {
            return null;
        }

    }

    class TestAnnotatedParameter extends AnnotatedParameter {

        @Override
        public AnnotatedWithParams getOwner() {
            return null;
        }

        @Override
        public int getIndex() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> anclz) {
            return null;
        }

        @Override
        public java.lang.annotation.Annotation[] getAnnotations() {
            return null;
        }

        @Override
        public java.lang.annotation.Annotation[] getDeclaredAnnotations() {
            return null;
        }

        @Override
        public AnnotatedElement getAnnotated() {
            return null;
        }
    }
}
