package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithEmptyListAndLimit() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertNull(pop.getFittestChromosome());
    }

    @Test
    public void testListPopulationWithNullListAndLimit() {
        try {
            new ListPopulation(null, 5);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithEmptyListAndNegativeLimit() {
        try {
            new ListPopulation(Collections.<Chromosome> emptyList(), -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithEmptyListAndZeroLimit() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 0);
        assertEquals(0, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertNull(pop.getFittestChromosome());
    }

    @Test
    public void testListPopulationWithListAndLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation pop = new ListPopulation(chromosomes, 5);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosomes.get(0), pop.getFittestChromosome());

        try {
            pop.setPopulationLimit(3);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        pop.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        assertEquals(2, pop.getPopulationSize());
        assertEquals(pop.getChromosomeList().get(1), pop.getFittestChromosome());
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        pop.setChromosomes(chromosomes);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosomes.get(0), pop.getFittestChromosome());
    }

    @Test
    public void testSetChromosomesNegativeSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        try {
            new ListPopulation(chromosomes).setChromosomes(chromosomes);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testSetChromosomesNullArgument() {
        try {
            new ListPopulation(Collections.<Chromosome> emptyList(), 5).setChromosomes(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.addChromosomes(chromosomes);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosomes.get(0), pop.getFittestChromosome());
    }

    @Test
    public void testAddChromosomesNullArgument() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        try {
            pop.addChromosomes(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddChromosomesLimitExceeded() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        try {
            pop.addChromosomes(chromosomes);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation pop = new ListPopulation(chromosomes, 5);
        List<Chromosome> result = pop.getChromosomes();
        assertSame(chromosomes, result);
        assertEquals(1, result.size());
        assertEquals(chromosomes.get(0), result.get(0));
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation pop = new ListPopulation(chromosomes, 5);
        List<Chromosome> result = pop.getChromosomeList();
        assertSame(chromosomes, result);
        assertEquals(1, result.size());
        assertEquals(chromosomes.get(0), result.get(0));
    }

    @Test
    public void testAddChromosome() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        Chromosome chromosome = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        pop.addChromosome(chromosome);
        assertEquals(5, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosome, pop.getFittestChromosome());
    }

    @Test
    public void testAddChromosomeLimitExceeded() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        Chromosome chromosome = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        try {
            pop.addChromosome(chromosome);
            pop.addChromosome(chromosome);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        ListPopulation pop = new ListPopulation(chromosomes, 5);
        assertEquals(2, pop.getPopulationSize());
        assertEquals(chromosomes.get(1), pop.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        assertEquals(5, pop.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        pop.setPopulationLimit(10);
        assertEquals(10, pop.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitNegative() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        try {
            pop.setPopulationLimit(-1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        ListPopulation pop = new ListPopulation(Collections.<Chromosome> emptyList(), 5);
        pop.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        pop.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        try {
            pop.setPopulationLimit(1);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome 1";
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome 2";
            }
        });
        ListPopulation pop = new ListPopulation(chromosomes, 5);
        assertEquals("[Chromosome 1, Chromosome 2]", pop.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        ListPopulation pop = new ListPopulation(chromosomes, 5);
        Iterator<Chromosome> it = pop.iterator();
        assertTrue(it.hasNext());
        Chromosome next = it.next();
        assertEquals(chromosomes.get(0), next);
        assertTrue(it.hasNext());
        next = it.next();
        assertEquals(chromosomes.get(1), next);
        assertFalse(it.hasNext());
    }
}