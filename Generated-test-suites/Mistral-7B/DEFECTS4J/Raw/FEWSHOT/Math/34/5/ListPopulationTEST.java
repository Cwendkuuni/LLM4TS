package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;

public class ListPopulationTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(-1);
    }

    @Test
    public void testConstructorEmptyListEmptyPopulation() {
        ListPopulation listPopulation = new ListPopulation(0);
        assertEquals(Collections.emptyList(), listPopulation.getChromosomes());
        assertEquals(0, listPopulation.getPopulationSize());
        assertEquals(0, listPopulation.getPopulationLimit());
    }

    @Test
    public void testConstructorNullListEmptyPopulation() {
        try {
            ListPopulation listPopulation = new ListPopulation(null, 10);
            fail("Should have thrown a NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorEmptyListPositivePopulation() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        assertEquals(Collections.emptyList(), listPopulation.getChromosomes());
        assertEquals(0, listPopulation.getPopulationSize());
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testConstructorNonEmptyListPopulationLimitSmaller() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        try {
            ListPopulation listPopulation = new ListPopulation(chromosomes, 1);
            fail("Should have thrown a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorNonEmptyListPopulationLimitBigger() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        ListPopulation listPopulation = new ListPopulation(chromosomes, 2);
        assertEquals(chromosomes, listPopulation.getChromosomes());
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(2, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetChromosomesNull() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        try {
            listPopulation.setChromosomes(null);
            fail("Should have thrown a NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetChromosomesSizeSmaller() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        try {
            listPopulation.setChromosomes(chromosomes);
            fail("Should have thrown a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testSetChromosomesSizeBigger() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        ListPopulation newListPopulation = new ListPopulation(chromosomes, 20);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
        assertEquals(20, listPopulation.getPopulationLimit());
    }

    @Test
    public void testAddChromosomesNull() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        try {
            listPopulation.addChromosomes(null);
            fail("Should have thrown a NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddChromosomesSizeSmaller() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        try {
            listPopulation.addChromosomes(chromosomes);
            fail("Should have thrown a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testAddChromosomesSizeBigger() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        listPopulation.addChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
        assertEquals(10 + chromosomes.size(), listPopulation.getPopulationSize());
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> expected = new ArrayList<>();
        listPopulation.addChromosome(expected.get(0) = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        assertEquals(expected, listPopulation.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> expected = new ArrayList<>();
        listPopulation.addChromosome(expected.get(0) = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        List<Chromosome> actual = listPopulation.getChromosomeList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddChromosomeNull() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        try {
            listPopulation.addChromosome(null);
            fail("Should have thrown a NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddChromosomePopulationLimitSmaller() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        };
        try {
            listPopulation.addChromosome(chromosome);
            fail("Should have thrown a NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testAddChromosomePopulationLimitBigger() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        Chromosome chromosome = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        };
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testGetFittestChromosomeNullPopulation() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        assertNull(listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetFittestChromosomeEmptyPopulation() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        Chromosome chromosome = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        };
        listPopulation.addChromosome(chromosome);
        assertEquals(chromosome, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetFittestChromosomeOneChromosome() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        };
        listPopulation.addChromosome(chromosome1);
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        };
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitNegative() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        try {
            listPopulation.setPopulationLimit(-1);
            fail("Should have thrown a NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testSetPopulationLimitSmallerCurrentSize() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        listPopulation.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        try {
            listPopulation.setPopulationLimit(5);
            fail("Should have thrown a NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSetPopulationLimitBiggerCurrentSize() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        listPopulation.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        listPopulation.setPopulationLimit(20);
        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(20, listPopulation.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        listPopulation.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        listPopulation.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }

            @Override
            public String toString() {
                return "Chromosome 1";
            }
        });
        listPopulation.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }

            @Override
            public String toString() {
                return "Chromosome 2";
            }
        });
        assertEquals("[Chromosome 1, Chromosome 2]", listPopulation.toString());
    }

    @Test
    public void testIteratorRemoveThrowsUnsupportedOperationException() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> chromosomes = new ArrayList<>();
        listPopulation.addChromosomes(chromosomes);
        Iterator<Chromosome> iterator = listPopulation.iterator();
        try {
            iterator.remove();
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

}