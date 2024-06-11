package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationInitialization() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertNull(pop.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationInitializationWithChromosomes() {
        Chromosome c1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        };
        ListPopulation pop = new ListPopulation(Arrays.asList(c1), 5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertEquals(c1, pop.getChromosomes().get(0));
    }

    @Test
    public void testListPopulationSetChromosomesWithNullList() {
        ListPopulation pop = new ListPopulation(5);
        assertThrows(NullArgumentException.class, () -> pop.setChromosomes(null));
    }

    @Test
    public void testListPopulationSetChromosomesWithListBiggerThanLimit() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[10]);
        assertThrows(NumberIsTooLargeException.class, () -> pop.setChromosomes(chromosomes));
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation pop = new ListPopulation(5);
        Chromosome c1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        };
        pop.addChromosomes(Collections.singletonList(c1));
        assertEquals(1, pop.getPopulationSize());
        assertEquals(c1, pop.getChromosomes().get(0));
    }

    @Test
    public void testListPopulationAddChromosomesWithNullList() {
        ListPopulation pop = new ListPopulation(5);
        assertThrows(NullArgumentException.class, () -> pop.addChromosomes(null));
    }

    @Test
    public void testListPopulationAddChromosomesWithListBiggerThanLimit() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[10]);
        assertThrows(NumberIsTooLargeException.class, () -> pop.addChromosomes(chromosomes));
    }

    @Test
    public void testListPopulationSetPopulationLimitWithNegativeValue() {
        ListPopulation pop = new ListPopulation(5);
        assertThrows(NotPositiveException.class, () -> pop.setPopulationLimit(-1));
    }

    @Test
    public void testListPopulationSetPopulationLimitWithCurrentSize() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        assertThrows(NumberIsTooSmallException.class, () -> pop.setPopulationLimit(4));
    }

    @Test
    public void testListPopulationSetPopulationLimitWithBiggerValue() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        pop.setPopulationLimit(6);
        assertEquals(1, pop.getPopulationSize());
        assertTrue(pop.getChromosomes().contains(pop.getChromosomes().get(0)));
        assertFalse(pop.getChromosomes().contains(pop.getChromosomes().get(0).clone()));
    }

    @Test
    public void testListPopulationClone() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        ListPopulation clonedPop = (ListPopulation) pop.clone();
        assertEquals(1, clonedPop.getPopulationSize());
        assertEquals(pop.getChromosomes().get(0), clonedPop.getChromosomes().get(0));
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }

            @Override
            public String toString() {
                return "Chromosome 1";
            }
        });
        assertEquals("[Chromosome 1]", pop.toString());
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        Iterator<Chromosome> it = pop.iterator();
        assertTrue(it.hasNext());
        Chromosome chromosome = it.next();
        assertEquals(pop.getChromosomes().get(0), chromosome);
        assertFalse(it.hasNext());
    }
}