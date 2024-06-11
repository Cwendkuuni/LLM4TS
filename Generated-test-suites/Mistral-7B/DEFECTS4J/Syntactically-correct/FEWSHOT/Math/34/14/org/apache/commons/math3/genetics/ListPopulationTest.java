package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationEmptyConstructor() {
        ListPopulation population = new ListPopulation(1);
        assertEquals(Collections.emptyList(), population.getChromosomes());
        assertEquals(1, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testListPopulationListConstructor() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        });
        ListPopulation population = new ListPopulation(chromosomes, 3);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(3, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullList() {
        new ListPopulation(null, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativeLimit() {
        new ListPopulation(Collections.emptyList(), -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCapacityExceeded() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[10000]);
        new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        });
        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(3, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 1);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCapacityExceededSetChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[10000]);
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        Chromosome chromosome = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        };
        population.addChromosomes(Arrays.asList(chromosome));
        List<Chromosome> expected = Arrays.asList(chromosome);
        assertEquals(expected, population.getChromosomes());
        assertEquals(3, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCapacityExceededAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[10000]);
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        });
        ListPopulation population = new ListPopulation(chromosomes, 3);
        List<Chromosome> expected = Arrays.asList(chromosomes.get(0), chromosomes.get(1));
        assertEquals(expected, population.getChromosomes());
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        });
        ListPopulation population = new ListPopulation(chromosomes, 3);
        List<Chromosome> expected = new ArrayList<>(population.getChromosomes());
        assertSame(expected, population.getChromosomeList());
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        Chromosome chromosome = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        };
        population.addChromosome(chromosome);
        List<Chromosome> expected = Arrays.asList(chromosome);
        assertEquals(expected, population.getChromosomes());
        assertEquals(3, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCapacityExceededAddChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        Chromosome chromosome = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        };
        population.addChromosome(chromosome);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        ListPopulation population = new ListPopulation(chromosomes, 3);
        Chromosome expected = chromosomes.get(1);
        assertEquals(expected, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        assertEquals(3, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativeLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        population.setPopulationLimit(-1);
    }

    @Test
    public void testListPopulationSetPopulationLimitSmallerCurrentSize() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[10]);
        ListPopulation population = new ListPopulation(chromosomes, 15);
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public String toString() {
                return "Chromosome 1";
            }
        }, new Chromosome() {
            @Override
            public String toString() {
                return "Chromosome 2";
            }
        });
        ListPopulation population = new ListPopulation(chromosomes, 3);
        assertEquals("[Chromosome 1, Chromosome 2]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }

            @Override
            public String toString() {
                return "Chromosome " + hashCode();
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }

            @Override
            public String toString() {
                return "Chromosome " + hashCode();
            }
        });
        ListPopulation population = new ListPopulation(chromosomes, 3);
        Iterator<Chromosome> iterator = population.iterator();
        Chromosome first = iterator.next();
        assertTrue(first.compareTo(population.getFittestChromosome()) < 0);
        Chromosome last = iterator.next();
        assertTrue(last.compareTo(population.getFittestChromosome()) > 0);
        assertFalse(iterator.hasNext());
    }
}