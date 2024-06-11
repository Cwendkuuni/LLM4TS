package org.scribe.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VerifierTest {

   @Mock
   Preconditions preconditionsMock;

   @Test
   public void testConstructor_ValidValue() {
      Verifier verifier = new Verifier("ValidValue");
      assertEquals("ValidValue", verifier.getValue());
   }

   @Test(expected = NullPointerException.class)
   public void testConstructor_NullValue() {
      Verifier verifier = new Verifier(null);
   }

   @Test
   public void testConstructor_WithPreconditionsCheck() {
      String value = "PreconditionsChecked";
      when(preconditionsMock.checkNotNull(value, "Must provide a valid string as verifier")).thenReturn(value);
   
      Verifier verifier = new Verifier(value);

      verify(preconditionsMock).checkNotNull(value, "Must provide a valid string as verifier");
      assertEquals(value, verifier.getValue());
   }

   @Test
   public void testGetValue() {
      String value = "TestValue";
      Verifier verifier = new Verifier(value);
      assertEquals(value, verifier.getValue());
   }

   @Test
   public void testGetValue_NullValue() {
      Verifier verifier = new Verifier(null);
      assertNull(verifier.getValue());
   }

}