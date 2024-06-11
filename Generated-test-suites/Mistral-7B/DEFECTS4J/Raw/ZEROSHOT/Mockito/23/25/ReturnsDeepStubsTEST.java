package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*; // junit-jupiter-engine version might require different imports

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.ReturnsDeepStubs;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;

 @BeforeEach
 void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 void testReturnsSameInstanceForSameCalls() {
     // given
     GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
         @Override
         public Type[] getComponentTypes() {
             return new Type[] {};
         }

         @Override
         public Class<?> getRawType() {
             return Map.class;
         }

         @Override
         public Class<?>[] getRawExtraInterfaces() {
             return new Class[] {Set.class};
         }
     };

     Object mockMap = mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
     Object mockSet = mock(Set.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

     // when
     Object mockEntrySet = mock(Map.EntrySet.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
     Object mockIterator = mock(Iterator.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
     Object mockValue = mock(Number.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

     when(mockEntrySet.iterator()).thenReturn(mockIterator);
     when(mockIterator.next()).thenReturn(mockMap.entrySet().iterator().next());

     // then
     assertEquals(mockMap, mockEntrySet.iterator().next().getValue());
     assertEquals(mockSet, mockMap.entrySet().iterator().next().getValue().iterator().next());
     assertEquals(mockValue, mockSet.iterator().next());

     assertSame(mockMap, mockEntrySet.iterator().next().getValue());
     assertSame(mockSet, mockMap.entrySet().iterator().next().getValue());
     assertSame(mockValue, mockSet.iterator().next());
 }

 @Test
 void testReturnsDifferentInstanceForDifferentCalls() {
     // given
     GenericMetadataSupport returnTypeGenericMetadata1 = new GenericMetadataSupport() {
         @Override
         public Type[] getComponentTypes() {
             return new Type[] {};
         }

         @Override
         public Class<?> getRawType() {
             return Map.class;
         }

         @Override
         public Class<?>[] getRawExtraInterfaces() {
             return new Class[] {Set.class};
         }
     };

     Object mockMap1 = mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

     GenericMetadataSupport returnTypeGenericMetadata2 = new GenericMetadataSupport() {
         @Override
         public Type[] getComponentTypes() {
             return new Type[] {};
         }

         @Override
         public Class<?> getRawType() {
             return Map.class;
         }

         @Override
         public Class<?>[] getRawExtraInterfaces() {
             return new Class[] {Set.class};
         }
     };

     Object mockMap2 = mock(Map.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

     // when
     Object mockEntrySet1 = mock(Map.EntrySet.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
     Object mockIterator1 = mock(Iterator.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
     Object mockValue1 = mock(Number.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

     when(mockEntrySet1.iterator()).thenReturn(mockIterator1);
     when(mockIterator1.next()).thenReturn(mockMap1.entrySet().iterator().next());

     Object mockEntrySet2 = mock(Map.EntrySet.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
     Object mockIterator2 = mock(Iterator.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
     Object mockValue2 = mock(Number.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

     when(mockEntrySet2.iterator()).thenReturn(mockIterator2);
     when(mockIterator2.next()).thenReturn(mockMap2.entrySet().iterator().next());

     // then
     assertNotSame(mockMap1, mockMap2);
     assertNotSame(mockMap1.entrySet().iterator().next().getValue(), mockMap2.entrySet().iterator().next().getValue());
     assertNotSame(mockValue1, mockValue2);
 }

 @Test
 void testDoesNotReturnStubForNonMockableTypes() {
     // given
     GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
         @Override
         public Type[] getComponentTypes() {
             return new Type[] {};
         }

         @Override
         public Class<?> getRawType() {
             return String.class;
         }

         @Override
         public Class<?>[] getRawExtraInterfaces() {
             return new Class[] {};
         }
     };

     Object mockString = mock(String.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

     // when
     Object result = returnsDeepStubs.answer(mockitoMock(InvocationOnMock.class)
             .withMethod( Collections.emptyList().getClass().getMethod("isEmpty", (Class[]) null))
             .withMock(mockString)
             .withArguments(Collections.emptyList())
             .withReturnType(Collections.emptyList().getClass()));

     // then
     assertNotNull(result);
     assertInstanceOf(Collections.emptyList.class, result);
     assertNotSame(mockString, result);
 }
}