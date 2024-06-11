/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    private InputStream inputStream;

    @Before
    public void setUp() {
        byte[] testData = {0x12, 0x34, 0x56, 0x78, 0x9A, 0xBC};
        inputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        inputStream.close();
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        // assert statements
    }

    @Test
    public void testReadBits() throws IOException {
        assertEquals(0x12, bitInputStream.readBits(8));
        assertEquals(0x123, bitInputStream.readBits(12));
        // assert additional test cases
    }
}