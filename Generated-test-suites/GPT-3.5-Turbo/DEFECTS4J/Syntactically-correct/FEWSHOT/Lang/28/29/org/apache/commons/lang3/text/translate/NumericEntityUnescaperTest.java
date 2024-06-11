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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslateValidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("This is a decimal entity: &#65;");
        
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a decimal entity: ");
        expectedOutput.append((char)65);
        
        assertEquals(expectedOutput.toString(), unescaper.translate(input, 0, null));
    }
    
    @Test
    public void testTranslateValidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("This is a hex entity: &#x41;");
        
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a hex entity: ");
        expectedOutput.append((char)65);
        
        assertEquals(expectedOutput.toString(), unescaper.translate(input, 0, null));
    }
    
    @Test
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("This is an invalid entity: &#XYZ;");
        
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is an invalid entity: ");
        
        assertEquals(expectedOutput.toString(), unescaper.translate(input, 0, null));
    }
    
    @Test
    public void testTranslateNoEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("This is a string with no entity");
        
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a string with no entity");
        
        assertEquals(expectedOutput.toString(), unescaper.translate(input, 0, null));
    }
}