
package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        // Create a Verifier object with a valid string value
        Verifier verifier = new Verifier("valid");
        
        // Verify that the getValue() method returns the correct value
        String expectedValue = "valid";
        String actualValue = verifier.getValue();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testConstructor_NullValue() {
        // Verify that the constructor throws an IllegalArgumentException
        try {
            new Verifier(null);
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testConstructor_EmptyValue() {
        // Verify that the constructor throws an IllegalArgumentException
        try {
            new Verifier("");
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
  
    @Test
    public void testConstructor_BlankValue() {
        // Verify that the constructor throws an IllegalArgumentException
        try {
            new Verifier(" ");
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
  
    @Test
    public void testConstructor_ValidValue() {
        // Create a Verifier object with a valid string value
        Verifier verifier = new Verifier("valid");
        
        // Verify that the getValue() method returns the correct value
        String expectedValue = "valid";
        String actualValue = verifier.getValue();
        Assert.assertEquals(expectedValue, actualValue);
    }
  
}
