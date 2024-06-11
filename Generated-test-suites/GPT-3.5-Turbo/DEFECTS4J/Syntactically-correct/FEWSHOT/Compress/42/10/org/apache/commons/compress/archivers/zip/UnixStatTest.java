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
package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnixStatTest {
    @Test
    public void testPermMask() {
        assertEquals(07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        assertEquals(040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }
}