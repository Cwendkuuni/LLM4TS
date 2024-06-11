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

package org.apache.commons.math.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {
    private MultidimensionalCounter multidimensionalCounter;

    @Before
    public void setUp() {
        multidimensionalCounter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testGetDimension() {
        int expected = 3;
        int result = multidimensionalCounter.getDimension();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetSize() {
        int expected = 24;
        int result = multidimensionalCounter.getSize();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetCounts() {
        int[] expected = {0, 0, 0};
        int[] result = multidimensionalCounter.getCounts(0);

        Assert.assertArrayEquals(expected, result);

        expected = new int[]{1, 0, 0};
        result = multidimensionalCounter.getCounts(3);

        Assert.assertArrayEquals(expected, result);

        expected = new int[]{1, 1, 0};
        result = multidimensionalCounter.getCounts(4);

        Assert.assertArrayEquals(expected, result);

        expected = new int[]{1, 2, 3};
        result = multidimensionalCounter.getCounts(23);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        multidimensionalCounter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange2() {
        multidimensionalCounter.getCounts(24);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        multidimensionalCounter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        multidimensionalCounter.getCount(-1, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange2() {
        multidimensionalCounter.getCount(0, 0, 4);
    }

    @Test
    public void testGetCount() {
        int expected = 0;
        int result = multidimensionalCounter.getCount(0, 0, 0);

        Assert.assertEquals(expected, result);

        expected = 1;
        result = multidimensionalCounter.getCount(0, 0, 1);

        Assert.assertEquals(expected, result);

        expected = 12;
        result = multidimensionalCounter.getCount(1, 0, 0);

        Assert.assertEquals(expected, result);

        expected = 23;
        result = multidimensionalCounter.getCount(1, 3, 2);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetSizes() {
        int[] expected = {2, 3, 4};
        int[] result = multidimensionalCounter.getSizes();

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int expected = count;
            int result = iterator.next();

            Assert.assertEquals(expected, result);

            count++;
        }
    }
}