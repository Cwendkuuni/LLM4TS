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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructor1() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testConstructor2() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructor3() {
        new ElitisticListPopulation(10, 1.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructor4() {
        new ElitisticListPopulation(10, -0.1);
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(0.9, population.getElitismRate(), 0);
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(1.1);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Population nextGeneration = population.nextGeneration();
        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        ElitisticListPopulation elitisticNextGeneration = (ElitisticListPopulation) nextGeneration;
        assertEquals(population.getPopulationLimit(), elitisticNextGeneration.getPopulationLimit());
        assertEquals(population.getElitismRate(), elitisticNextGeneration.getElitismRate(), 0);
        assertTrue(elitisticNextGeneration.getChromosomes().size() < population.getChromosomes().size());
        assertTrue(elitisticNextGeneration.getChromosomes().containsAll(population.getChromosomes()));
    }

    class Chromosome {
    }

}