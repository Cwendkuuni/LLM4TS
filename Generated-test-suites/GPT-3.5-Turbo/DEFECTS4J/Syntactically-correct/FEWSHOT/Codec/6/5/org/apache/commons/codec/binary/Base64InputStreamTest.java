
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

package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws Exception {
        byte[] input = new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in);
        
        int result = base64In.read();
        
        assertEquals("Expected result to be equal to 'A'", 'A', result);
    }
    
    @Test
    public void testReadWithEmptyInput() throws Exception {
        byte[] input = new byte[] {};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in);
        
        int result = base64In.read();
        
        assertEquals("Expected result to be equal to -1", -1, result);
    }
    
    @Test
    public void testReadWithInvalidInput() throws Exception {
        byte[] input = new byte[] {(byte) 0xFF};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in);
        
        int result = base64In.read();
        
        assertEquals("Expected result to be equal to -1", -1, result);
    }
    
    @Test
    public void testReadWithEncodedData() throws Exception {
        byte[] input = new byte[] {0x41, 0x42, 0x43};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in, true);
        
        int result = base64In.read();
        
        assertEquals("Expected result to be equal to 65", 65, result);
    }
    
    @Test
    public void testReadWithDecodedData() throws Exception {
        byte[] input = new byte[] {0x41, 0x42, 0x43};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        int result = base64In.read();
        
        assertEquals("Expected result to be equal to 65", 65, result);
    }
    
    @Test
    public void testReadWithInvalidData() throws Exception {
        byte[] input = new byte[] {(byte) 0xFF};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        int result = base64In.read();
        
        assertEquals("Expected result to be equal to -1", -1, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws Exception {
        byte[] input = new byte[] {0x41, 0x42, 0x43};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        base64In.read(null, 0, 0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws Exception {
        byte[] input = new byte[] {0x41, 0x42, 0x43};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = new byte[3];
        base64In.read(buffer, -1, 3);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws Exception {
        byte[] input = new byte[] {0x41, 0x42, 0x43};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = new byte[3];
        base64In.read(buffer, 0, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferLength() throws Exception {
        byte[] input = new byte[] {0x41, 0x42, 0x43};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in, false);
        
        byte[] buffer = new byte[2];
        base64In.read(buffer, 0, 3);
    }
    
    @Test
    public void testMarkSupported() throws Exception {
        byte[] input = new byte[] {0x41, 0x42, 0x43};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in);
        
        boolean result = base64In.markSupported();
        
        assertFalse("Expected result to be false", result);
    }
}
