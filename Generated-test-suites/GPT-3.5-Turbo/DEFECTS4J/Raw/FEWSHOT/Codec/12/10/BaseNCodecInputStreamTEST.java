
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
    
    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;
    
    @Before
    public void setup() {
        baseNCodec = new BaseNCodec() {
            
            @Override
            void encode(byte[] src, int off, int len, BaseNCodecContext context) {
                // do nothing
            }
            
            @Override
            void decode(byte[] src, int off, int len, BaseNCodecContext context) {
                // do nothing
            }
            
            @Override
            void encode(byte[] src, int off, int len) throws IOException {
                // do nothing
            }
            
            @Override
            void decode(byte[] src, int off, int len) throws IOException {
                // do nothing
            }
            
            @Override
            protected boolean isInAlphabet(byte octet) {
                // always return true for testing purposes
                return true;
            }
        };
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[]{0x66, 0x6F, 0x6F};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        assertEquals(102, baseNCodecInputStream.read());
        assertEquals(111, baseNCodecInputStream.read());
        assertEquals(111, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }
    
    @Test
    public void testReadWithZeroBytes() throws IOException {
        byte[] data = new byte[]{};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        assertEquals(-1, baseNCodecInputStream.read());
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = new byte[]{0x66, 0x6F, 0x6F};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        byte[] buffer = new byte[2];
        int len = baseNCodecInputStream.read(buffer, 0, buffer.length);
        
        assertEquals(2, len);
        assertEquals(102, buffer[0]);
        assertEquals(111, buffer[1]);
    }
    
    @Test
    public void testReadWithSkip() throws IOException {
        byte[] data = new byte[]{0x66, 0x6F, 0x6F};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        long skipped = baseNCodecInputStream.skip(2);
        assertEquals(2, skipped);
        
        assertEquals(111, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }
    
    @Test
    public void testMarkSupported() {
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{}), baseNCodec, false);
        assertFalse(baseNCodecInputStream.markSupported());
    }
    
    @Test
    public void testAvailable() throws IOException {
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{}), baseNCodec, false);
        assertEquals(0, baseNCodecInputStream.available());
        
        byte[] data = new byte[]{0x66, 0x6F, 0x6F};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        assertEquals(3, baseNCodecInputStream.available());
        
        baseNCodecInputStream.read();
        
        assertEquals(2, baseNCodecInputStream.available());
        
        baseNCodecInputStream.read(new byte[2], 0, 2);
        
        assertEquals(0, baseNCodecInputStream.available());
        
        baseNCodecInputStream.read();
        
        assertEquals(0, baseNCodecInputStream.available());
    }
    
    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        
        baseNCodecInputStream.close();
        
        assertTrue(in.read() == -1);
    }

}
