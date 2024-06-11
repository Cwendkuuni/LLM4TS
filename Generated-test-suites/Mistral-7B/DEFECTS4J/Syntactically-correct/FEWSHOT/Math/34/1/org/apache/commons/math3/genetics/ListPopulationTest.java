package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
    public void testListPopulationEmptyConstructor() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertNull(pop.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationWithChromosomesConstructor() {
        Chromosome chromo1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromo1);

        ListPopulation pop = new ListPopulation(chromosomes, 5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertSame(chromo1, pop.getChromosomes().get(0));
    }

    @Test
    public void testSetChromosomes() {
        Chromosome chromo1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        Chromosome chromo2 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return -1;
            }
        };
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromo1);
        chromosomes.add(chromo2);

        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.setChromosomes(chromosomes);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(2, pop.getPopulationSize());
        assertSame(chromo1, pop.getChromosomes().get(0));
        assertSame(chromo2, pop.getChromosomes().get(1));
    }

    @Test
    public void testAddChromosomes() {
        Chromosome chromo1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        Chromosome chromo2 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return -1;
            }
        };
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.addChromosomes(Collections.singletonList(chromo1));
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertSame(chromo1, pop.getChromosomes().get(0));

        pop.addChromosomes(Collections.singletonList(chromo2));
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(2, pop.getPopulationSize());
        assertSame(chromo1, pop.getChromosomes().get(0));
        assertSame(chromo2, pop.getChromosomes().get(1));
    }

    @Test
    public void testGetChromosomes() {
        Chromosome chromo1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        Chromosome chromo2 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return -1;
            }
        };
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromo1);
        chromosomes.add(chromo2);

        ListPopulation pop = new ListPopulation(chromosomes, 5);
        List<Chromosome> chromosomesList = pop.getChromosomes();
        assertEquals(2, chromosomesList.size());
        assertSame(chromo1, chromosomesList.get(0));
        assertSame(chromo2, chromosomesList.get(1));
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromo1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.addChromosome(chromo1);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertSame(chromo1, pop.getChromosomes().get(0));
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromo1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        Chromosome chromo2 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return -1;
            }
        };
        ListPopulation pop = new ListPopulation(Arrays.asList(chromo1, chromo2), 5);
        Chromosome fittest = pop.getFittestChromosome();
        assertEquals(chromo1, fittest);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(5, pop.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitNotNull() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.setPopulationLimit(10);
        assertEquals(10, pop.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitNull() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        assertThrows(NullArgumentException.class, () -> pop.setPopulationLimit(null));
    }

    @Test
    public void testSetPopulationLimitSmallerCurrentSize() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Chromosome()), 5);
        assertThrows(NumberIsTooSmallException.class, () -> pop.setPopulationLimit(3));
    }

    @Test
    public void testSetPopulationLimitLargerCurrentSize() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Chromosome()), 5);
        pop.addChromosome(new Chromosome());
        pop.setPopulationLimit(7);
        assertEquals(5, pop.getPopulationSize());
    }

    @Test
    public void testToString() {
        Chromosome chromo1 = new Chromosome() {
            @Override
            public String toString() {
                return "Chromosome1";
            }
        };
        Chromosome chromo2 = new Chromosome() {
            @Override
            public String toString() {
                return "Chromosome2";
            }
        };
        ListPopulation pop = new ListPopulation(Arrays.asList(chromo1, chromo2), 5);
        assertEquals("[Chromosome1, Chromosome2]", pop.toString());
    }

    @Test
    public void testIterator() {
        Chromosome chromo1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        Chromosome chromo2 = new Chromosome() {
            @Override
            public int compareTo(Chromosome other) {
                return -1;
            }
        };
        ListPopulation pop = new ListPopulation(Arrays.asList(chromo1, chromo2), 5);
        Iterator<Chromosome> iterator = pop.iterator();
        assertTrue(iterator.hasNext());
        assertSame(chromo1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(chromo2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddChromosomesNullArgument() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Chromosome()), 5);
        assertThrows(NullArgumentException.class, () -> pop.addChromosomes(null));
    }

    @Test
    public void testAddChromosomesTooManyChromosomes() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Chromosome()), 5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomesToAdd.add(new Chromosome());
        }
        assertThrows(NumberIsTooLargeException.class, () -> pop.addChromosomes(chromosomesToAdd));
    }

    @Test
    public void testAddChromosomesLessChromosomesThanCapacity() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Chromosome()), 5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(new Chromosome());
        pop.addChromosomes(chromosomesToAdd);
        assertEquals(2, pop.getPopulationSize());
    }

    @Test
    public void testAddChromosomesExactSize() {
        ListPopulation pop = new ListPopulation(Collections.singletonList(new Chromosome()), 5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomesToAdd.add(new Chromosome());
        }
        pop.addChromosomes(chromosomesToAdd);
        assertEquals(5, pop.getPopulationSize());
    }

}