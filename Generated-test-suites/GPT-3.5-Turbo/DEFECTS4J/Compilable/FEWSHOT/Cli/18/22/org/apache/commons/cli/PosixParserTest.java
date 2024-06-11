
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", false, "A simple option");

        PosixParser parser = new PosixParser();

        String[] arguments = new String[]{"-f"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);

        arguments = new String[]{"-ff"};
        result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-f", result[1]);

        arguments = new String[]{"-f", "-ff", "-fff"};
        result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-f", result[1]);
        assertEquals("-f", result[2]);

        arguments = new String[]{"-f", "value"};
        result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);

        arguments = new String[]{"-f", "value1", "value2"};
        result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);

        arguments = new String[]{"-f", "--", "value"};
        result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--", result[1]);
        assertEquals("value", result[2]);
    }
}
