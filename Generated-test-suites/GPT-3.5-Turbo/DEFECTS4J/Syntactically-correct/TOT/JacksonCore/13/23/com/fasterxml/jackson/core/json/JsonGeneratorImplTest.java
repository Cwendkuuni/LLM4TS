package com.fasterxml.jackson.core.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        // Arrange
        JsonGeneratorImpl generator = new JsonGeneratorImpl();

        // Act
        generator.writeStringField("fieldName", "value");

        // Assert
        // Verify that the field name and value are correctly written
        Assert.assertEquals("\"fieldName\":\"value\"", generator.toString());
    }

    @Test
    public void testWriteStringField_NullFieldName() {
        // Arrange
        JsonGeneratorImpl generator = new JsonGeneratorImpl();

        // Act
        generator.writeStringField(null, "value");

        // Assert
        // Verify that the field name is written as null and value is correctly written
        Assert.assertEquals("\"null\":\"value\"", generator.toString());
    }

    @Test
    public void testWriteStringField_NullValue() {
        // Arrange
        JsonGeneratorImpl generator = new JsonGeneratorImpl();

        // Act
        generator.writeStringField("fieldName", null);

        // Assert
        // Verify that the field name is correctly written and value is written as null
        Assert.assertEquals("\"fieldName\":null", generator.toString());
    }

    @Test
    public void testWriteStringField_EmptyFieldName() {
        // Arrange
        JsonGeneratorImpl generator = new JsonGeneratorImpl();

        // Act
        generator.writeStringField("", "value");

        // Assert
        // Verify that the field name is written as empty and value is correctly written
        Assert.assertEquals("\"\":\"value\"", generator.toString());
    }

    @Test
    public void testWriteStringField_EmptyValue() {
        // Arrange
        JsonGeneratorImpl generator = new JsonGeneratorImpl();

        // Act
        generator.writeStringField("fieldName", "");

        // Assert
        // Verify that the field name is correctly written and value is written as empty
        Assert.assertEquals("\"fieldName\":\"\"", generator.toString());
    }

    @Test
    public void testWriteStringField_EscapeCharacters() {
        // Arrange
        JsonGeneratorImpl generator = new JsonGeneratorImpl();

        // Act
        generator.writeStringField("fieldName", "\\\"value\\\"");

        // Assert
        // Verify that the field name and value with escape characters are correctly written
        Assert.assertEquals("\"fieldName\":\"\\\\\\\"value\\\\\\\"\"", generator.toString());
    }
}