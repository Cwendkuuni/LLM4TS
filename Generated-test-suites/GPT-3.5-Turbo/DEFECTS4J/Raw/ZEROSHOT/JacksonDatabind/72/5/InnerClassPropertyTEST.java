package com.fasterxml.jackson.databind.deser.impl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty() {
			
			@Override
			public void set(Object instance, Object value) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object setAndReturn(Object instance, Object value) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <A extends Annotation> A getAnnotation(Class<A> acls) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getPropertyIndex() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void assignIndex(int index) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AnnotatedMember getMember() {
				// TODO Auto-generated method stub
				return null;
			}
		};
        Constructor<?> ctor = new Constructor<?>();
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithAnnotatedConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty() {
			
			@Override
			public void set(Object instance, Object value) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object setAndReturn(Object instance, Object value) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <A extends Annotation> A getAnnotation(Class<A> acls) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getPropertyIndex() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void assignIndex(int index) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AnnotatedMember getMember() {
				// TODO Auto-generated method stub
				return null;
			}
		};
        AnnotatedConstructor ann = new AnnotatedConstructor(null);
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ann);
        
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
        assertEquals(ann, innerClassProperty._annotated);
    }
    
    @Test
    public void testConstructorWithJsonDeserializer() {
        SettableBeanProperty delegate = new SettableBeanProperty() {
			
			@Override
			public void set(Object instance, Object value) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object setAndReturn(Object instance, Object value) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <A extends Annotation> A getAnnotation(Class<A> acls) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getPropertyIndex() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void assignIndex(int index) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AnnotatedMember getMember() {
				// TODO Auto-generated method stub
				return null;
			}
		};
        JsonDeserializer<?> deser = new JsonDeserializer<?>();
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, deser);
        
        assertNotNull(innerClassProperty);
        assertEquals(delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithPropertyName() {
        SettableBeanProperty delegate = new SettableBeanProperty() {
			
			@Override
			public void set(Object instance, Object value) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object setAndReturn(Object instance, Object value) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <A extends Annotation> A getAnnotation(Class<A> acls) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getPropertyIndex() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void assignIndex(int index) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AnnotatedMember getMember() {
				// TODO Auto-generated method stub
				return null;
			}
		};
        PropertyName newName = new PropertyName("newName");
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, newName);
        
        assertNotNull(innerClassProperty);
        assertEquals(delegate.withName(newName), innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
    }

    // Add more test methods for other methods in InnerClassProperty class
}