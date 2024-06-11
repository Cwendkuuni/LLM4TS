/*
 *  Copyright 2001-2007 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LenientDateTimeFieldTest {

    private static final Chronology BASE = LenientChronology.getInstance();

    private DateTimeField strictField;
    private DateTimeField lenientField;

    @Before
    public void setUp() {
        strictField = new StrictDateTimeField(BASE.minutes(), BASE);
        lenientField = LenientDateTimeField.getInstance(strictField, BASE);
    }

    @Test
    public void testGetInstance() {
        assertEquals(strictField, LenientDateTimeField.getInstance(strictField, BASE));
        assertEquals(lenientField, LenientDateTimeField.getInstance(lenientField, BASE));
    }

    @Test
    public void testIsLenient() {
        assertEquals(false, strictField.isLenient());
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = BASE.getDateTimeMillis(2019, 10, 1, 0, 0, 0, 0);

        // Test setting value within bounds
        assertEquals(instant + 60 * BaseSingleField.LENGTH, lenientField.set(instant, 1));

        // Test setting value beyond bounds
        long outOfBoundsValue = 100;
        long expectedValue = instant + (outOfBoundsValue - 60) * BaseSingleField.LENGTH;
        assertEquals(expectedValue, lenientField.set(instant, (int) outOfBoundsValue));
    }

}