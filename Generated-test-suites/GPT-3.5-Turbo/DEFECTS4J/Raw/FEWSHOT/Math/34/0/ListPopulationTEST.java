
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

package org.apache.commons.math3.genetics;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ListPopulationTest {
    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
    }

    @Test
    public void testConstructor() {
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(0, listPopulation.getPopulationSize());
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorZeroPopulationLimit() {
        new ListPopulation(0);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimitWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, -5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorZeroPopulationLimitWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorChromosomesExceededPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 1);
    }
    
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullChromosomes() {
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceededPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceededPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        List<Chromosome> result = listPopulation.getChromosomes();
        assertEquals(chromosomes, result);
        // Make sure the returned list is unmodifiable
        try {
            result.add(new Chromosome());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = listPopulation.getChromosomeList();
        assertEquals(Collections.emptyList(), chromosomes);
        // Add chromosome to the returned list
        Chromosome c = new Chromosome();
        chromosomes.add(c);
        assertEquals(Collections.singletonList(c), listPopulation.getChromosomes());
    }

    @Test
    public void testAddChromosome() {
        Chromosome c = new Chromosome();
        listPopulation.addChromosome(c);
        assertEquals(Collections.singletonList(c), listPopulation.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceededPopulationLimit() {
        for (int i = 0; i < 5; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
        listPopulation.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();
        c1.setFitness(1);
        c2.setFitness(2);
        c3.setFitness(3);
        List<Chromosome> chromosomes = Arrays.asList(c1, c2, c3);
        listPopulation.setChromosomes(chromosomes);
        Chromosome result = listPopulation.getFittestChromosome();
        assertEquals(c3, result);
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetNegativePopulationLimit() {
        listPopulation.setPopulationLimit(-5);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetZeroPopulationLimit() {
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        listPopulation.addChromosome(new Chromosome());
        listPopulation.setPopulationLimit(0);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());
        listPopulation.addChromosome(new Chromosome());
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        assertEquals("[]", listPopulation.toString());
        Chromosome c = new Chromosome();
        listPopulation.addChromosome(c);
        assertEquals("[" + c.toString() + "]", listPopulation.toString());
    }

    @Test
    public void testIterator() {
        assertFalse(listPopulation.iterator().hasNext());

        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        listPopulation.addChromosome(c1);
        listPopulation.addChromosome(c2);

        List<Chromosome> chromosomes = Arrays.asList(c1, c2);
        for (Chromosome chromosome : listPopulation) {
            assertTrue(chromosomes.contains(chromosome));
        }
    }
}
