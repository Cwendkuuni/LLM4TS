package geo.google.mapping;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        // Create GeoAddress object for testing
        GeoAddress address = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        address.setAddressDetails(addressDetails);
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);

        // Set country name code
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);

        // Set administrative area
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);

        // Set administrative area name
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminAreaNames.add(adminAreaName);
        adminArea.setAdministrativeAreaName(adminAreaNames);

        // Set locality
        Locality locality = new Locality();
        adminArea.setLocality(locality);

        // Set locality name
        List<Locality.LocalityName> localityNames = new ArrayList<>();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        localityNames.add(localityName);
        locality.setLocalityName(localityNames);

        // Set postal code
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCodeNumbers.add(postalCodeNumber);
        postalCode.setPostalCodeNumber(postalCodeNumbers);

        // Set thoroughfare
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        List<ThoroughfareNameType> thoroughfareNames = new ArrayList<>();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("123 Main St");
        thoroughfareNames.add(thoroughfareName);
        thoroughfare.setThoroughfareName(thoroughfareNames);

        // Test AddressToUsAddressFunctor.execute() method
        GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("USA");
        expected.setState("California");
        expected.setCity("Los Angeles");
        expected.setPostalCode("90001");
        expected.setAddressLine1("123 Main St");

        GeoUsAddress result = functor.execute(address);

        assertEquals(expected, result);
    }
}