package org.apache.commons.math3.genetics;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationCreationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationCreationWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationCreationWithInitialChromosomes() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }}, 10);

        assertEquals(10, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
        assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationCreationWithNullInitialChromosomesList() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCreationWithInitialChromosomesListBiggerThanPopulationLimit() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});
        Chromosome chromosome3 = new Chromosome(new float[]{7.0f, 8.0f, 9.0f});

        List<Chromosome> initialChromosomes = new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
            add(chromosome3);
        }};

        ListPopulation population = new ListPopulation(initialChromosomes, 3);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 10);
        population.setChromosomes(Collections.singletonList(chromosome1));

        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullChromosomesList() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithChromosomesListBiggerThanPopulationLimit() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});
        Chromosome chromosome3 = new Chromosome(new float[]{7.0f, 8.0f, 9.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 3);
        population.setChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
            add(chromosome3);
        }});
    }

    @Test
    public void testListPopulationAddChromosomes() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 10);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }});

        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
        assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationAddChromosomesWithNullChromosomesList() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 10);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithChromosomesListBiggerThanPopulationLimit() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});
        Chromosome chromosome3 = new Chromosome(new float[]{7.0f, 8.0f, 9.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 3);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
            add(chromosome3);
        }});
    }

    @Test
    public void testListPopulationGetChromosomes() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }}, 5);

        List<Chromosome> chromosomes = population.getChromosomes();

        assertEquals(2, chromosomes.size());
        assertEquals(chromosome1, chromosomes.get(0));
        assertEquals(chromosome2, chromosomes.get(1));
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }}, 5);

        List<Chromosome> chromosomeList = population.getChromosomeList();

        assertEquals(2, chromosomeList.size());
        assertEquals(chromosome1, chromosomeList.get(0));
        assertEquals(chromosome2, chromosomeList.get(1));
    }

    @Test
    public void testListPopulationAddChromosome() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome populationChromosome = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 5);
        population.addChromosome(chromosome1);

        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithPopulationLimitReached() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome populationChromosome = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 2);
        population.addChromosome(chromosome1);
        population.addChromosome(populationChromosome);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(Collections.singletonList(chromosome1), 5);

        Chromosome fittest = population.getFittestChromosome();

        assertEquals(chromosome1, fittest);
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 5);

        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 5);

        population.setPopulationLimit(10);

        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(), 5);

        population.setPopulationLimit(-1);
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }}, 5);

        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        Chromosome chromosome1 = new Chromosome(new float[]{1.0f, 2.0f, 3.0f});
        Chromosome chromosome2 = new Chromosome(new float[]{4.0f, 5.0f, 6.0f});

        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(chromosome1);
            add(chromosome2);
        }}, 5);

        String expected = "[Chromosome[fitness=0.0, genome=[1.0, 2.0, 3.0f]], Chromosome[fitness=0.0, genome=[4.0, 5.0, 6.0f]]]";

        assertEquals(expected, population.toString());
    }
}