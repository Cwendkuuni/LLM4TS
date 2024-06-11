
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
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithNumber() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Test with decimal number
        String input1 = "&#65;";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, writer);
        assertEquals(6, result1);
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);

        // Test with hex number
        String input2 = "&#x41;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer);
        assertEquals(7, result2);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Test with invalid number format
        String input = "&#XYZ;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());

        // Test with invalid starting characters
        String input2 = "&;#65;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer);
        assertEquals(0, result2);
        assertEquals("", writer.toString());

        // Test with no ending semicolon
        String input3 = "&#65";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, writer);
        assertEquals(0, result3);
        assertEquals("", writer.toString());
    }
}

