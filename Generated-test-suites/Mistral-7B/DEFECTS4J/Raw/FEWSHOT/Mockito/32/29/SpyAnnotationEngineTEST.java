package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.Runners;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

 @Test
 public void testProcess_NoSpyAnnotation() {
     Class<?> context = Mockito.class;
     Object testClass = new Object();

     Field[] fields = context.getDeclaredFields();
     SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

     spyAnnotationEngine.process(context, testClass);

     // Verify that no fields were spied
     for (Field field : fields) {
         Assert.assertNull(field.getAnnotation(Spy.class));
     }
 }

 @Test
 public void testProcess_SingleSpyAnnotation() {
     Class<?> context = Mockito.class;
     Object testClass = new Object();

     @Spy
     Field spyField = new Field() {
         {
             name = "testField";
         }
     };
     spyField.setAccessible(true);
     try {
         spyField.set(testClass, new Object());
     } catch (IllegalAccessException e) {
         // Ignore
     }
     Field[] fields = context.getDeclaredFields();

     spyAnnotationEngine.process(context, testClass);

     // Verify that the field was spied
     Field actualSpyField = null;
     for (Field field : fields) {
         if (field.getName().equals("testField")) {
             actualSpyField = field;
             break;
         }
     }
     Assert.assertNotNull(actualSpyField);
     Assert.assertEquals(Spy.class, actualSpyField.getAnnotation(Spy.class));
     Object instance = null;
     try {
         instance = actualSpyField.get(testClass);
     } catch (IllegalAccessException e) {
         // Ignore
     }
     Assert.assertNotNull(instance);
     Assert.assertFalse(MockUtil.isMock(instance));
 }

 @Test
 public void testProcess_MultipleSpyAnnotation() {
     Class<?> context = Mockito.class;
     Object testClass = new Object();

     @Spy
     Field spyField1 = new Field() {
         {
             name = "testField1";
         }
     };
     spyField1.setAccessible(true);
     try {
         spyField1.set(testClass, new Object());
     } catch (IllegalAccessException e) {
         // Ignore
     }

     @Spy
     Field spyField2 = new Field() {
         {
             name = "testField2";
         }
     };
     spyField2.setAccessible(true);
     try {
         spyField2.set(testClass, new Object());
     } catch (IllegalAccessException e) {
         // Ignore
     }

     Field[] fields = context.getDeclaredFields();

     spyAnnotationEngine.process(context, testClass);

     // Verify that both fields were spied
     Field actualSpyField1 = null;
     Field actualSpyField2 = null;
     for (Field field : fields) {
         if (field.getName().equals("testField1")) {
             actualSpyField1 = field;
         } else if (field.getName().equals("testField2")) {
             actualSpyField2 = field;
         }
     }
     Assert.assertNotNull(actualSpyField1);
     Assert.assertEquals(Spy.class, actualSpyField1.getAnnotation(Spy.class));
     Assert.assertNotNull(actualSpyField2);
     Assert.assertEquals(Spy.class, actualSpyField2.getAnnotation(Spy.class));
 }

 @Test
 public void testProcess_MockAnnotationPresent() {
     Class<?> context = Mockito.class;
     Object testClass = new Object();

     @Mock
     Field mockField = new Field() {
         {
             name = "testField";
         }
     };
     mockField.setAccessible(true);
     try {
         mockField.set(testClass, Mockito.mock(String.class));
     } catch (IllegalAccessException e) {
         // Ignore
     }
     Field[] fields = context.getDeclaredFields();

     spyAnnotationEngine.process(context, testClass);

     // Verify that the field was not spied
     for (Field field : fields) {
         if (field.getName().equals("testField")) {
             Assert.assertNull(field.getAnnotation(Spy.class));
             Assert.assertEquals(Mock.class, field.getAnnotation(Mock.class));
         }
     }
 }

 @Test(expected = MockitoException.class)
 public void testProcess_InstanceMissing() {
     Class<?> context = Mockito.class;
     Object testClass = new Object();

     @Spy
     Field spyField = new Field() {
         {
             name = "testField";
         }
     };
     spyField.setAccessible(true);

     Field[] fields = context.getDeclaredFields();

     spyAnnotationEngine.process(context, testClass);
 }

 @Test
 public void testProcess_AccessibleFlag() {
     Class<?> context = Mockito.class;
     Object testClass = new Object();

     @Spy
     Field spyField = new Field() {
         {
             name = "testField";
         }
     };
     spyField.setAccessible(true);
     try {
         spyField.set(testClass, new Object());
     } catch (IllegalAccessException e) {
         // Ignore
     }

     Field[] fields = context.getDeclaredFields();

     spyAnnotationEngine.process(context, testClass);

     // Verify that the field was spied
     Field actualSpyField = null;
     for (Field field : fields) {
         if (field.getName().equals("testField")) {
             actualSpyField = field;
             break;
         }
     }
     Assert.assertNotNull(actualSpyField);
     Assert.assertEquals(Spy.class, actualSpyField.getAnnotation(Spy.class));
 }

 @Test
 public void testProcess_DuplicateAnnotation() {
     Class<?> context = Mockito.class;
     Object testClass = new Object();

     @Spy
     Field spyField = new Field() {
         {
             name = "testField";
         }
     };
     spyField.setAccessible(true);
     try {
         spyField.set(testClass, new Object());
     } catch (IllegalAccessException e) {
         // Ignore
     }

     Field[] fields = context.getDeclaredFields();

     spyAnnotationEngine.process(context, testClass);

     // Verify that the field was spied exactly once
     int spyAnnotationCount = 0;
     for (Field field : fields) {
         if (field.getName().equals("testField")) {
             if (field.getAnnotation(Spy.class) != null) {
                 spyAnnotationCount++;
             }
         }
     }
     Assert.assertEquals(1, spyAnnotationCount);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testProcess_InvalidAnnotationCombination() {
     Class<?> context = Mockito.class;
     Object testClass = new Object();

     @Spy
     Field spyField = new Field() {
         {
             name = "testField";
         }
     };
     spyField.setAccessible(true);
     try {
         spyField.set(testClass, new Object());
     } catch (IllegalAccessException e) {
         // Ignore
     }

     @Mock
     Field mockField = new Field() {
         {
             name = "testField";
         }
     };
     mockField.setAccessible(true);
     try {
         mockField.set(testClass, new Object());
     } catch (IllegalAccessException e) {
         // Ignore
     }

     Field[] fields = context.getDeclaredFields();

     spyAnnotationEngine.process(context, testClass);
 }
}