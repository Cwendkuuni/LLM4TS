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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = {
            {"foo", "bar"},
            {"baz", "qux"},
            {"abc", "def"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringBuffer stringBuffer = new StringBuffer();
        translator.translate("foo", 0, new StringWriter(stringBuffer));
        assertEquals("bar", stringBuffer.toString());

        stringBuffer = new StringBuffer();
        translator.translate("baz", 0, new StringWriter(stringBuffer));
        assertEquals("qux", stringBuffer.toString());

        stringBuffer = new StringBuffer();
        translator.translate("abc", 0, new StringWriter(stringBuffer));
        assertEquals("def", stringBuffer.toString());

        stringBuffer = new StringBuffer();
        translator.translate("def", 0, new StringWriter(stringBuffer));
        assertEquals("", stringBuffer.toString());
    }

    @Test
    public void testTranslateMultipleMatches() throws IOException {
        CharSequence[][] lookup = {
            {"foo", "bar"},
            {"foobar", "qux"},
            {"abc", "def"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringBuffer stringBuffer = new StringBuffer();
        translator.translate("foobar", 0, new StringWriter(stringBuffer));
        assertEquals("qux", stringBuffer.toString());

        stringBuffer = new StringBuffer();
        translator.translate("abcfoobar", 0, new StringWriter(stringBuffer));
        assertEquals("defbar", stringBuffer.toString());

        stringBuffer = new StringBuffer();
        translator.translate("abcfoobardef", 0, new StringWriter(stringBuffer));
        assertEquals("defbardef", stringBuffer.toString());
    }

    @Test
    public void testTranslateNoMatch() throws IOException {
        CharSequence[][] lookup = {
            {"foo", "bar"},
            {"baz", "qux"},
            {"abc", "def"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringBuffer stringBuffer = new StringBuffer();
        translator.translate("xyz", 0, new StringWriter(stringBuffer));
        assertEquals("", stringBuffer.toString());
    }

    @Test
    public void testTranslateEmptyInput() throws IOException {
        CharSequence[][] lookup = {
            {"foo", "bar"},
            {"baz", "qux"},
            {"abc", "def"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringBuffer stringBuffer = new StringBuffer();
        translator.translate("", 0, new StringWriter(stringBuffer));
        assertEquals("", stringBuffer.toString());
    }

    @Test
    public void testTranslateShortInput() throws IOException {
        CharSequence[][] lookup = {
            {"foo", "bar"},
            {"baz", "qux"},
            {"abc", "def"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringBuffer stringBuffer = new StringBuffer();
        translator.translate("a", 0, new StringWriter(stringBuffer));
        assertEquals("", stringBuffer.toString());
    }

    @Test
    public void testTranslateNullInput() throws IOException {
        CharSequence[][] lookup = {
            {"foo", "bar"},
            {"baz", "qux"},
            {"abc", "def"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringBuffer stringBuffer = new StringBuffer();
        translator.translate(null, 0, new StringWriter(stringBuffer));
        assertEquals("", stringBuffer.toString());
    }
}