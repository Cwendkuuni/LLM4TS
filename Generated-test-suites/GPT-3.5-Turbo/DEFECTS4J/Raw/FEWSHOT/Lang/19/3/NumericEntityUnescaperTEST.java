/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_withValidInputNoHexadecimal() throws IOException {
        // Arrange
        CharSequence input = "&#65;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withValidInputHexadecimal() throws IOException {
        // Arrange
        CharSequence input = "&#x41;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withValidInputAndOptionalSemicolon() throws IOException {
        // Arrange
        CharSequence input = "&#65";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(4, result);
        assertEquals('A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withInvalidInput() throws IOException {
        // Arrange
        CharSequence input = "&#invalid;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withMultipleEntities() throws IOException {
        // Arrange
        CharSequence input = "&#65;&#x62;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals('A', writer.toString().charAt(0));
        assertEquals('b', writer.toString().charAt(1));
    }

    @Test
    public void testTranslate_withInvalidInputAndValidInput() throws IOException {
        // Arrange
        CharSequence input = "&#invalid;&#65;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}